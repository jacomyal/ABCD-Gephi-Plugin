/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package io.jcml.gephi.plugins.friendships;

import io.jcml.gephi.plugins.friendships.Friendships;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsBuilder;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author jacomyal
 */
@ServiceProvider(service = StatisticsBuilder.class)
public class FriendshipsBuilder implements StatisticsBuilder {

    @Override
    public String getName() {
        return "Reverse";//NbBundle.getMessage(ReverseBuilder.class, "Reverse.name");
    }

    @Override
    public Statistics getStatistics() {
        return new Friendships();
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Friendships.class;
    }
}
