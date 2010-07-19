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
package com.linebee.solrmeter.controller;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.linebee.solrmeter.model.SolrMeterConfiguration;
import com.linebee.solrmeter.model.statistic.CommitHistoryStatistic;
import com.linebee.solrmeter.model.statistic.ErrorLogStatistic;
import com.linebee.solrmeter.model.statistic.FullQueryStatistic;
import com.linebee.solrmeter.model.statistic.HistogramQueryStatistic;
import com.linebee.solrmeter.model.statistic.OperationTimeHistory;
import com.linebee.solrmeter.model.statistic.QueryLogStatistic;
import com.linebee.solrmeter.model.statistic.QueryTimeHistoryStatistic;
import com.linebee.solrmeter.model.statistic.SimpleOptimizeStatistic;
import com.linebee.solrmeter.model.statistic.SimpleQueryStatistic;
import com.linebee.solrmeter.model.statistic.TimeRangeStatistic;
import com.linebee.solrmeter.view.statistic.ErrorLogPanel;
import com.linebee.solrmeter.view.statistic.FullQueryStatisticPanel;
import com.linebee.solrmeter.view.statistic.HistogramChartPanel;
import com.linebee.solrmeter.view.statistic.OperationTimeLineChartPanel;
import com.linebee.solrmeter.view.statistic.PieChartPanel;
import com.linebee.solrmeter.view.statistic.QueryTimeHistoryPanel;
/**
 * Repository for all the available statistics
 * @author tflobbe
 *
 */
@Singleton
public class StatisticsRepository {
	
	private List<StatisticDescriptor> availableStatistics;
	
	@Inject
	public StatisticsRepository() {
		super();
		availableStatistics = new LinkedList<StatisticDescriptor>();
		loadDefaults();
	}

	protected void loadDefaults() {
		availableStatistics.add(new StatisticDescriptor("histogram","Histogram of Queries", HistogramQueryStatistic.class, HistogramChartPanel.class,new StatisticType[]{StatisticType.QUERY}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("timeRange","Shows Query Times as a pie chart", TimeRangeStatistic.class, PieChartPanel.class,new StatisticType[]{StatisticType.QUERY}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("errorLog","Shows the log of all produced errors", ErrorLogStatistic.class, ErrorLogPanel.class,new StatisticType[]{StatisticType.QUERY, StatisticType.UPDATE, StatisticType.OPTIMIZE}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("queryHistort","Shows a chart with the past query times", QueryTimeHistoryStatistic.class, QueryTimeHistoryPanel.class,new StatisticType[]{StatisticType.QUERY}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("operationHistory","Shows the a chart with the executed operations and times", OperationTimeHistory.class, OperationTimeLineChartPanel.class, new StatisticType[]{StatisticType.QUERY, StatisticType.UPDATE, StatisticType.OPTIMIZE}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("fullQueryStatistic","Shows advanced statistics of queries plus the last executed queries", FullQueryStatistic.class, FullQueryStatisticPanel.class, new StatisticType[]{StatisticType.QUERY}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("QueryLogStatistic", QueryLogStatistic.class, new StatisticType[]{StatisticType.QUERY}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("CommitHistoryStatistic", CommitHistoryStatistic.class, new StatisticType[]{StatisticType.UPDATE}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("SimpleOptimizeStatistic", SimpleOptimizeStatistic.class, new StatisticType[]{StatisticType.OPTIMIZE}, StatisticScope.STRESS_TEST));
		availableStatistics.add(new StatisticDescriptor("SimpleQueryStatistic", SimpleQueryStatistic.class, new StatisticType[]{StatisticType.QUERY}, StatisticScope.STRESS_TEST));
	}

	/**
	 * Add a statistic description. This statistic will be available
	 * to be enabled from the UI.
	 * @param description
	 */
	public void addStatistic(StatisticDescriptor description) {
		availableStatistics.add(description);
	}

	public List<StatisticDescriptor> getAvailableStatistics() {
		return availableStatistics;
	}

	public List<StatisticDescriptor> getActiveStatistics() {
		String showingStatisticsString = SolrMeterConfiguration.getProperty("statistic.showingStatistics");
		List<StatisticDescriptor> list = new LinkedList<StatisticDescriptor>();
		if(showingStatisticsString == null || showingStatisticsString.isEmpty() || showingStatisticsString.equalsIgnoreCase("all")) {
			list.addAll(availableStatistics);
			return list;
		}
		Set<String> set = new HashSet<String>();
		String[] statisticsNames = showingStatisticsString.split(",");
		for(String name:statisticsNames) {
			set.add(name.trim());
		}
		for(StatisticDescriptor description:availableStatistics) {
			if(set.contains(description.getName()) || !description.isHasView()) {
				list.add(description);
			}
		}
		return list;
	}
}
