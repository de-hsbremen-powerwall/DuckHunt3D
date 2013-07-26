package de.hsbremen.powerwall.duckhunt3d.objects;


/**
 * creates player with score bullets and shells
 * 
 * @author HDDesign
 *
 */
public class Player {
	private int score;
	private int bullets;
	private int shells;
	private String name;
	
	/**
	 * 
	 * @param pNumber
	 *	
	 */
	public Player(int pNumber){
		score = 0;
		bullets = 6;
		shells = 0;
		name = String.valueOf(pNumber);
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score += score;
	}

	public int getBullets() {
		return bullets;
	}

	public void setBullets(int bullets) {
		this.bullets -= bullets;
	}

	public void reload() {
		bullets = 6;
		this.shells = 0;
	}

	public void addShell() {
		if(this.bullets == 0 && this.shells < 6)
			this.shells +=1;
	}

	public int getShells() {
		return shells;
	}

	public String getName() {
		return name;
	}

}
