import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 *
 * THIS VERSION IS STILL KINDA MESSY
 * BUT THI GENETIC ALGORITHM HAS BEEN CREATED BY ME
 * JUST WITH KNOWLEDGE AND NOT LOOKING AT ANY CODE
 *
 * Credits to Giuseppe(Beppe) Marinelli
 */

public class Practical2 {

	static final String TARGET = "HELLO WORLD";
	static final double mutationRate = 0.2;
	static final int popSize = 100;

	static char[] alphabet = new char[27];
	static ArrayList<Individual> population;

	static Random generator = new Random(System.currentTimeMillis());

	//MAIN VOID THAT STARTS ALL
	public static void main(String[] args) {

		//INITIALIZE THE ALFABETH AND THE INITIAL POPULATION
		initialize(popSize, TARGET.length());

		// WHAT THE POPULATION LOOK LIKE?
		for (int i = 0; i < population.size(); i++) {
			System.out.println("n: " +i + " " +population.get(i).genoToPhenotype());
		}

		// CALCULATE TIME FROM THE START TO THE END
		final long startTime = System.nanoTime();

		int iterations =0;
		while (!draw())
			iterations++;

		final double duration = (double)(System.nanoTime() - startTime)/1000000000;
		System.out.println("n of iterations: " + iterations + " ,time passed: " + duration +" seconds");
	}

	//Method to attuate the genethic algorithm
	public static boolean draw(){

		//SELECTION
		int[][] sortedFitn = calcFitness(population);
		//go out the loop when the fitness of the first is == Target.length
		if(sortedFitn[sortedFitn.length-1][1] == TARGET.length()) return true;

		//REPRODUCTION
		reproduCe(sortedFitn);

		return false;
	}

	static public void reproduCe(int[][] sortedFitness){
		// SELECT PARENTS
		ArrayList<int[]> selected = new ArrayList<>();

		for(int i=0; i<sortedFitness.length; i++)
			if (sortedFitness[i][1] >= 1)
				selected.add(new int[]{sortedFitness[i][0], sortedFitness[i][1]});

		//now i select two random number between 0 and 99 in tmp
		//random.nextInt(max - min + 1) + min

		//int first = selected.get(generator.nextInt(selected.size()))[0];
		//int second = selected.get(generator.nextInt(selected.size()))[0];

		int first = selected.get(selected.size()-2)[0];
		int second = selected.get(selected.size()-1)[0];

		//CROSSOVER AND MUTATION
		char[] son = generateSon(population.get(first).chromosome, population.get(second).chromosome);

		population.add(new Individual(son));

		//PRINT OUT THE LAST SON GENERATED
		System.out.println("son " + population.get(population.size()-1).genoToPhenotype()
				+ " par: "+first + ", " +second );
	}

	static public char[] generateSon(char[] f1, char[] f2){
		// CROSSOVER
		char[] son = new char[TARGET.length()];
		int middle = Math.round(TARGET.length()/2);
		for (int i=0; i< TARGET.length(); i++){
			if(i<=middle) son[i] = f1[i];
			else son[i] = f2[i];
		}

		// MUTATION
		if(Math.random() <= mutationRate)
			son[generator.nextInt(son.length)] = genChars(1)[0];

		return son;
	}

	//function to calculate the fitness
	static public int[][] calcFitness(ArrayList<Individual> population){
		int[][] fitness = new int[population.size()][2];

		//create the id array
		for(int i=0; i< population.size(); i++){
			fitness[i][0] = i ;

			//calculate the fitness of the population array and put into fitness
			int c=0;
			for(int j=0; j<TARGET.length(); j++){
				if(population.get(i).getChromosome()[j] == TARGET.charAt(j))
					c++;
			}
			fitness[i][1] = c;
			//System.out.println("Fid "+i + " f: " +c);
		}

		/*
		for (Individual i : population) {
			if(i.fitness == 1000000){
				//this means i have to calculate its fitness
				//calculate the fitness of the population array and put into fitness
				int c=0;
				for(int j=0; j<TARGET.length(); j++){
					if(i.getChromosome()[j] == TARGET.charAt(j))
						c++;
				}
				i.fitness = c;
			}
		}*/

		//sort by fitness while keeping the ids
		Arrays.sort(fitness, Comparator.comparingInt(arr -> arr[1]));

		return fitness;
	}

	static public char[] genChars(int targetSize){

		char[] tempChromosome = new char[targetSize];
		for (int j = 0; j < targetSize; j++) {
			tempChromosome[j] = alphabet[generator.nextInt(alphabet.length)]; //choose a random letter in the alphabet
		}
		return tempChromosome;
	}

	static private void initialize(int size, int targetSize){
		//GENERATE THE ALFABETH
		for (char c = 'A'; c <= 'Z'; c++) {
			alphabet[c - 'A'] = c;
		}
		alphabet[26] = ' ';

		//GENERATE THE INITIAL POPULATION
		population = new ArrayList<>();

		// we initialize the population with random characters
		for (int i = 0; i < size; i++) {
			population.add(new Individual(genChars(targetSize)));
		}
	}
}
