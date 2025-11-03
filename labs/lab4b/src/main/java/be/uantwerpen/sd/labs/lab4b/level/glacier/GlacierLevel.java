package be.uantwerpen.sd.labs.lab4b.level.glacier;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.Level;

public final class GlacierLevel extends Level {
    @Override
    public int extraPullBias() {
        return AppConfig.get().glacierPullBonus;
    }
}