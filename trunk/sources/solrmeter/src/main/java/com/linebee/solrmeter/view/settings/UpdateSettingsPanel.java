/**
 * Copyright Linebee LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linebee.solrmeter.view.settings;

import javax.swing.BoxLayout;

import com.linebee.solrmeter.controller.SettingsController;
import com.linebee.solrmeter.view.I18n;
import com.linebee.solrmeter.view.SettingsPanel;
import com.linebee.solrmeter.view.component.BooleanPropertyPanel;
import com.linebee.solrmeter.view.component.ComboPropertyPanel;
import com.linebee.solrmeter.view.component.FilePropertyPanel;
import com.linebee.solrmeter.view.component.TextPropertyPanel;
import com.linebee.solrmeter.view.exception.InvalidPropertyException;
import com.linebee.solrmeter.view.listener.PropertyChangeListener;

public class UpdateSettingsPanel extends SettingsPanel implements PropertyChangeListener {
	
	private static final long serialVersionUID = -1176374120440517941L;
	private SettingsController controller;
	private boolean editable;

	@Override
	public String getSettingsName() {
		return I18n.get("settings.update.title");
	}
	
	public UpdateSettingsPanel(SettingsController controller, boolean editable) {
		super();
		this.controller = controller;
		this.editable = editable;
		this.initGUI();
	}

	private void initGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new TextPropertyPanel(I18n.get("settings.update.solrUrl"), "solr.addUrl", editable, this));
		this.add(new BooleanPropertyPanel(I18n.get("settings.update.solrAutocommit"), "solr.update.solrAutocommit", editable, this));
		this.add(new TextPropertyPanel(I18n.get("settings.update.documentsToCommit"), "solr.update.documentsToCommit",editable, this));
		this.add(new TextPropertyPanel(I18n.get("settings.update.timeToCommit"), "solr.update.timeToCommit",editable, this));
		this.add(new FilePropertyPanel(I18n.get("settings.update.updateFile"), "solr.updatesFiles", editable, this));
		this.add(new ComboPropertyPanel(I18n.get("settings.update.updateExecutor"), "executor.updateExecutor", editable, this, new String[]{"random", "constant"}));
		
	}

	@Override
	public void onPropertyChanged(String property, String text)
			throws InvalidPropertyException {
		controller.setProperty(property, text);
	}

}
