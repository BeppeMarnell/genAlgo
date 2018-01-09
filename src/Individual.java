
public class Individual {
	
	private char[] chromosome;
	private int fitness;
	boolean initializated;
	
	public Individual(char[] chromosome) {
		this.chromosome = chromosome;
		this.fitness = 0;
		this.initializated = false;
	}


	public char[] getChromosome() {
		return chromosome;
	}

	public void setChromosome(char[] chromosome) {
		this.chromosome = chromosome;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {

		this.fitness = fitness;
		this.initializated = true;
	}
	
	public String genoToPhenotype() {
		StringBuilder builder = new StringBuilder();
		builder.append(chromosome);
		return builder.toString();
	}
	
	public Individual clone() {
		char[] chromClone = new char[chromosome.length];
		for(int i = 0; i < chromClone.length; i++) {
			chromClone[i] = chromosome[i];
		}
		return new Individual(chromClone);
	}
	


}
