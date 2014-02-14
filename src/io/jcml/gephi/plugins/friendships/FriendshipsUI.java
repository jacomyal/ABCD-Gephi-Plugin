/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.jcml.gephi.plugins.friendships;

import javax.swing.JPanel;
import org.gephi.statistics.spi.Statistics;
import org.gephi.statistics.spi.StatisticsUI;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StatisticsUI.class)
public class FriendshipsUI implements StatisticsUI {

    private final StatSettings settings = new StatSettings();
    private FriendshipsPanel panel;
    private Friendships reverse;

    @Override
    public JPanel getSettingsPanel() {
        panel = new FriendshipsPanel();
        return panel;
    }

    @Override
    public void setup(Statistics statistics) {
        this.reverse = (Friendships) statistics;
        if (panel != null) {
            settings.load(reverse);
            panel.setP0Ratio(reverse.getP0Ratio());
            panel.setP1Ratio(reverse.getP1Ratio());
            panel.setP2Ratio(reverse.getP2Ratio());
            panel.setP3Ratio(reverse.getP3Ratio());
        }
    }

    @Override
    public void unsetup() {
        if (panel != null) {
            reverse.setP0Ratio(panel.getP0Ratio());
            reverse.setP1Ratio(panel.getP1Ratio());
            reverse.setP2Ratio(panel.getP2Ratio());
            reverse.setP3Ratio(panel.getP3Ratio());
            settings.save(reverse);
        }
        panel = null;
        reverse = null;
    }

    @Override
    public Class<? extends Statistics> getStatisticsClass() {
        return Friendships.class;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "ReverseUI";//NbBundle.getMessage(getClass(), "ReverseUI.name");
    }

    @Override
    public String getCategory() {
        return StatisticsUI.CATEGORY_NETWORK_OVERVIEW;
    }

    @Override
    public int getPosition() {
        return 800;
    }

    @Override
    public String getShortDescription() {
        return "TODO";//NbBundle.getMessage(getClass(), "ReverseUI.shortDescription");
    }

    private static class StatSettings {

        private float p0Ratio = -2;
        private float p1Ratio = 1;
        private float p2Ratio = 1;
        private float p3Ratio = 1;

        private void save(Friendships stat) {
            this.p0Ratio = stat.getP0Ratio();
            this.p1Ratio = stat.getP1Ratio();
            this.p2Ratio = stat.getP2Ratio();
            this.p3Ratio = stat.getP3Ratio();
        }

        private void load(Friendships stat) {
            stat.setP0Ratio(p0Ratio);
            stat.setP1Ratio(p1Ratio);
            stat.setP2Ratio(p2Ratio);
            stat.setP3Ratio(p3Ratio);
        }
    }
}