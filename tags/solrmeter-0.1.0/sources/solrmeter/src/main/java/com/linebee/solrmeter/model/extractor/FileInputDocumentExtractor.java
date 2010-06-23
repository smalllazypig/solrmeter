/**
 * Copyright Linebee. www.linebee.com
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
package com.linebee.solrmeter.model.extractor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.common.SolrInputDocument;

import com.linebee.solrmeter.model.FileUtils;
import com.linebee.solrmeter.model.InputDocumentExtractor;
/**
 * Extracts documents from text files. The file must be fieldName:fieldValue;fieldName2:fieldValue2;...
 * and all required fields must be added.
 * if a ";" character is part of some value (and it is not a field separator) it must be scaped with a "\" character.
 * All "\" on a value must be scaped as "\\"
 * @see com.linebee.solrmeter.extractor.FileInputDocumentExtractorTestCase.testScapedChars()
 * @author Tomas
 *
 */
public class FileInputDocumentExtractor implements InputDocumentExtractor {
	
	private final static Logger logger = Logger.getLogger(FileInputDocumentExtractor.class);
	
	/**
	 * The list of extracted documents
	 */
	protected List<SolrInputDocument> documents;
	
	public FileInputDocumentExtractor(File inputFile) {
		super();
		documents = new LinkedList<SolrInputDocument>();
		loadDocuments(inputFile);
	}
	
	/**
	 * Loads all documents from text file
	 */
	protected void loadDocuments(File inputFile) {
		List<String> documentStrings = FileUtils.loadStringsFromFile(inputFile.getPath());
		documents = this.createDocumentList(documentStrings);
	}
	
	private List<SolrInputDocument> createDocumentList(List<String> documentsStrings) {
		List<SolrInputDocument> list = new LinkedList<SolrInputDocument>();
		for(String documentString:documentsStrings) {
			list.add(this.createSolrDocument(documentString));
		}
		return list;
	}
	
	private SolrInputDocument createSolrDocument(String documentString) {
		SolrInputDocument document = new SolrInputDocument();
		List<String> fields = this.split(documentString);
		try {
			for(String field:fields) {
				try {
					document.addField(field.substring(0, field.indexOf(":")), field.substring(field.indexOf(":") + 1));
				}catch(RuntimeException e) {
					logger.error("Error Loading documents, on field " + field);
					throw e;
				}
			}
		} catch(RuntimeException e) {
			logger.error("Error Loading documents, on document line: " + documentString);
			throw e;
		}
		
		return document;
	}
	
	private List<String> split(String documentString) {
		List<String> strings = new LinkedList<String>();
		int lastSplitIndex = 0;
		int nextSplitIndex;
		while(lastSplitIndex < documentString.length()) {
			nextSplitIndex = findNextSplitIndex(documentString, lastSplitIndex);
			String splittedString = documentString.substring(lastSplitIndex, nextSplitIndex);
			strings.add(removeEscapeCharacters(splittedString));
			lastSplitIndex = nextSplitIndex + 1;
		}
		return strings;
	}
	
	private String removeEscapeCharacters(String splittedString) {
		return splittedString.replaceAll("\\\\;", ";").replaceAll("\\\\\\\\", "\\\\");
	}

	/**
	 * Returns the next Index to Split the String
	 * @param documentString
	 * @param lastSplitIndex
	 * @return
	 */
	private int findNextSplitIndex(String documentString, int lastSplitIndex) {
		for(int i = lastSplitIndex; i < documentString.length(); i++) {
			if(documentString.charAt(i) == '\\') {
				if(documentString.charAt(i + 1) == '\\' || documentString.charAt(i + 1) == ';') {
					i++;
				}
			}else {
				if(documentString.charAt(i) == ';') {
					return i;
				}
			}
		}
		return documentString.length();
	}

	@Override
	public SolrInputDocument getRandomDocument() {
		return (SolrInputDocument) FileUtils.getNextRandomObject(documents);
	}

}