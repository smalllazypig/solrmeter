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
package com.linebee.solrmeter;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.linebee.solrmeter.model.FieldExtractor;
import com.linebee.solrmeter.model.InputDocumentExtractor;
import com.linebee.solrmeter.model.OptimizeExecutor;
import com.linebee.solrmeter.model.QueryExecutor;
import com.linebee.solrmeter.model.QueryExtractor;
import com.linebee.solrmeter.model.SolrMeterConfiguration;
import com.linebee.solrmeter.model.UpdateExecutor;
import com.linebee.solrmeter.model.executor.QueryExecutorRandomImpl;
import com.linebee.solrmeter.model.executor.UpdateExecutorRandomImpl;
import com.linebee.solrmeter.model.extractor.FileFieldExtractor;
import com.linebee.solrmeter.model.extractor.FileInputDocumentExtractor;
import com.linebee.solrmeter.model.extractor.FileQueryExtractor;
/**
 * 
 * @author tflobbe
 *
 */
public class ModelModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(QueryExecutor.class).to(QueryExecutorRandomImpl.class);
		bind(UpdateExecutor.class).to(UpdateExecutorRandomImpl.class);
		bind(OptimizeExecutor.class);
	}
	
	@Provides @Named("queryExtractor")
	public QueryExtractor createQueryExtractor() {
		return new FileQueryExtractor(SolrMeterConfiguration.getProperty(SolrMeterConfiguration.QUERIES_FILE_PATH));
	}
	
	@Provides @Named("filterQueryExtractor")
	public QueryExtractor createFilterQueryExtractor() {
		return new FileQueryExtractor(SolrMeterConfiguration.getProperty("solr.query.filterQueriesFile"));
	}
	
	@Provides
	public FieldExtractor createFieldExtractor() {
		return new FileFieldExtractor(SolrMeterConfiguration.getProperty(SolrMeterConfiguration.FIELDS_FILE_PATH));
	}
	
	@Provides @Named("updateExtractor")
	public InputDocumentExtractor createInputExtractor() {
		return new FileInputDocumentExtractor(SolrMeterConfiguration.getProperty(SolrMeterConfiguration.UPDATES_FILE_PATH));
	}

}