/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.jcml.gephi.plugins.abcd;

import io.jcml.gephi.plugins.abcd.ABCD;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jacomyal
 */
@ServiceProvider(service = StatisticsBuilder.class)
public class ABCDBuilder implements StatisticsBuilder {

    @Override
    public String getName() {
        return "ABCD Algorithm";
    }

    @Override
    public Statistics getStatistics() {
        return new ABCD();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return ABCD.class;
    }
}
