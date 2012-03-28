package org.laserforce.scaling.common;

public final class PlayerDetail implements Comparable<PlayerDetail> {

	private final AveragesForPlayer originalAverages;
    private final AveragesForPlayer transformedAverages;
	private final double average;
	private final int nrGamesPlayed;
    private final int nrGamesTransformed;

    public PlayerDetail(AveragesForPlayer originalAverages, AveragesForPlayer transformedAverages, double average, int nrGamesPlayed, int nrGamesTransformed) {
		this.originalAverages = originalAverages;
        this.transformedAverages = transformedAverages;
		this.average = average;
		this.nrGamesPlayed = nrGamesPlayed;
        this.nrGamesTransformed = nrGamesTransformed;
	}

    public String getAthleteName() {
        return originalAverages.getAthlete().getCodeName();
    }

	public PlayerAverage getOriginalAverageFor(LeaguePosition leaguePosition) {
		return originalAverages.getAverageFor(leaguePosition);
	}

    /**
     * Determine whether this Player has transformed averages for he given LeaguePosition.
     * <p>
     * This probably indicates that the player has fewer than the required number of games for the LeaguePosition.
     * </p> 
     *
     * @param leaguePosition LeaguePosition for which to determine whether this Player has no transformed averages.
     * @return true if this Player has no transformed average for the supplied LeaguePosition.
     */
    public boolean hasNoTransformedAverageFor(LeaguePosition leaguePosition) {
        return transformedAverages.getAverageFor(leaguePosition) == null;
    }

    public PlayerAverage getTransformedAverageFor(LeaguePosition leaguePosition) {
        return transformedAverages.getAverageFor(leaguePosition);
    }

	public Athlete getAthlete() {
		return originalAverages.getAthlete();
	}

	public double getAverage() {
		return average;
	}

	public int getNrGamesPlayed() {
		return nrGamesPlayed;
	}

    public int getNrGamesTransformed() {
        return nrGamesTransformed;
    }

    @Override
    /**
     * Compares this PlayerDetail with another based upon their (scaled) average.
     */
	public int compareTo(PlayerDetail other) {
        return (int) ((other.average - average) * 1000);
	}

	@Override
    public String toString() {
        return originalAverages.getAthlete() +
                "{avg=" + (int) average +
                ", nrGames=" + nrGamesPlayed +
//                ", nrGamesTransformed=" + nrGamesTransformed +
//                ", originalAverages=" + originalAverages +
//                ", transformedAverages=" + transformedAverages +
                '}';
    }
}
