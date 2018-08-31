/*******************************************************************************
 * Copyright (c) 2018 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtend.ide.swtbot;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Test;


/**
 * @author dietrich - Initial contribution and API
 */
public class ViechTest {
	
	static String file = "package demo;\n\n"
			+ "/**\n"
			+ " * <script>alert('xxxx');</script>DEMO\n"
			+ " */\n"
			+ "public class Demo {\n"
			+ "\n"
			+ "}\n"
			;

	@Test
	public void testHover() throws ParseException {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		final SWTBotShell shell = bot.activeShell();
		for (SWTBotView view : bot.views()) {
			if ("Welcome".equals(view.getTitle())) {
				view.close();
			}
		}
		bot.perspectiveByLabel("Java").activate();
		fileNew(bot, "Project...");
		bot.shell("New Project").activate();
		expandNode(bot.tree(), "Java").select("Java Project");
		bot.button("Next >").click();
		SWTBotText _textWithLabel = bot.textWithLabel("Project name:");
		_textWithLabel.setText("demo");
		bot.button("Finish").click();
		SWTBotEclipseEditor newJavaEditor = newJavaEditor(bot, "Demo", "demo", "demo/src");
		setContent(newJavaEditor, file);
		newJavaEditor.save();
		
		for (int i=0; i<1000;i++) {
			
			bot.menu("Navigate").menu("Open Resource...").click();
			bot.shell("Open Resource").activate();
			bot.text().setText("Demo.java");
			bot.button("Open").click();
			SWTBotEclipseEditor editor = bot.editorByTitle("Demo.java").toTextEditor();
			editor.selectRange(5, 15, 0);
			bot.sleep(50);
			editor.pressShortcut(KeyStroke.getInstance("F2"));
			bot.sleep(1000);
			editor.close();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		System.out.println("xxx");

	}
	
	  public static SWTBotTreeItem expandNode(final AbstractSWTBot<?> bot, final String node) {
		    SWTBotTreeItem item = null;
		    if ((bot instanceof SWTBotTree)) {
		      item = ((SWTBotTree)bot).getTreeItem(node);
		    } else {
		      if ((bot instanceof SWTBotTreeItem)) {
		        item = ((SWTBotTreeItem)bot).getNode(node);
		      }
		    }
		    boolean _isExpanded = item.isExpanded();
		    boolean _not = (!_isExpanded);
		    if (_not) {
		      item.expand();
		    }
		    return item;
		  }
	
	
	
	public static void setContent(final SWTBotEclipseEditor it, final CharSequence content) {
	    final String contentString = content.toString();
	    it.setText(contentString);
	  }
	
	public static SWTBotEclipseEditor newJavaEditor(final SWTWorkbenchBot it, final String typeName, final String packageName, final String sourceFolderPath) {
	    SWTBotEclipseEditor _xblockexpression = null;
	    {
	      fileNew(it, "Class");
	      it.shell("New Java Class").activate();
	      SWTBotText _textWithLabel = it.textWithLabel("Source folder:");
	      _textWithLabel.setText(sourceFolderPath);
	      SWTBotText _textWithLabel_1 = it.textWithLabel("Package:");
	      _textWithLabel_1.setText(packageName);
	      SWTBotText _textWithLabel_2 = it.textWithLabel("Name:");
	      _textWithLabel_2.setText(typeName);
	      it.button("Finish").click();
	      _xblockexpression = it.editorByTitle((typeName + ".java")).toTextEditor();
	    }
	    return _xblockexpression;
	  }

	protected static void fileNew(final SWTWorkbenchBot it, final String newWhat) {
		int retries = 3;
		for (int i=0;i<3;i++) {
			try {
				it.menu("File").menu("New").menu(newWhat).click();
				return;
			} catch (final Throwable _t) {
				if (_t instanceof WidgetNotFoundException) {
					final WidgetNotFoundException e = (WidgetNotFoundException) _t;
					if (i == (retries - 1)) {
						throw e;
					}
					String _message = e.getMessage();
					String _plus = ("failed: " + _message);
					System.out.println(_plus);
					System.out.println("retrying...");
					it.sleep(1000);
				} else {
					throw new RuntimeException(_t);
				}
			}
		}
	}

}