import java.util.*;

/**
 * Some very basic stuff to get you started. It shows basically how each
 * chromosome is built.
 *
 * THIS VERSION IS STILL KINDA MESSY
 * BUT THI GENETIC ALGORITHM HAS BEEN CREATED BY ME
 * JUST WITH KNOWLEDGE AND NOT LOOKING AT ANY CODE
 *
 * Credits to Giuseppe(Beppe) Marinelli
 *
 * best result :
 * pop size = 100
 * mutation rate = 0.5
 * n of iterations: 59 ,time passed: 0.051656752 seconds
 */

public class Practical2 {

	static final String TARGET = "HELLO WORLD";
	static final double mutationRate = 0.5;
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

		int iterations =0;

		// CALCULATE TIME FROM THE START TO THE END
		final long startTime = System.nanoTime();

		while (!draw())
			iterations++;

		final double duration = (double)(System.nanoTime() - startTime)/1000000000;
		System.out.println("n of iterations: " + iterations + " ,time passed: " + duration +" seconds");

	}

	//Method to attuate the genethic algorithm
	public static boolean draw(){

		//SELECTION
		if(calcFitness(population))return true;

		//REPRODUCTION
		reproduCe();

		return false;
	}

	static public void reproduCe(){
		// SELECT PARENTS
		ArrayList<int[]> selected = new ArrayList<>();

		for(int i=0; i<population.size(); i++)
			if (population.get(i).getFitness() >= 1)
				selected.add(new int[]{i, population.get(i).getFitness()});

		//Sorting the array list selected referring to the highest fitness
		Collections.sort(selected, new Comparator<int[]>() {
			@Override
			public int compare(int[] z1, int[] z2) {
				if (z1[1] > z2[1])
					return 1;
				if (z1[1] < z2[1])
					return -1;
				return 0;
			}
		});
		//random.nextInt(max - min + 1) + min

		int first = selected.get(selected.size()-1)[0];
		int second = selected.get(selected.size()-2)[0];

		//CROSSOVER AND MUTATION
		char[] son = generateSon(population.get(first).getChromosome(), population.get(second).getChromosome());

		//extended search for optimization result
		int fSon = fastFitness(son);//

		int max = Math.max(fastFitness(population.get(first).getChromosome()),
				fastFitness(population.get(second).getChromosome()));

		int count = 1;
		while (fSon < max){
			//re-take the second
			second = selected.get(selected.size()-(2+count))[0];
			//generate another son
			son = generateSon(population.get(first).getChromosome(), population.get(second).getChromosome());
			//calcualate this fitness' son
			fSon = fastFitness(son);
			//calculate again the maximum
			max = Math.max(fastFitness(population.get(first).getChromosome()),
					fastFitness(population.get(second).getChromosome()));
		}


		population.add(new Individual(son));

		//PRINT OUT THE LAST SON GENERATED
		System.out.println("son " + population.get(population.size()-1).genoToPhenotype()
				+ " parents: "+first + ", " +second );
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
		for(int i=0; i<TARGET.length(); i++)
			if(Math.random() <= mutationRate)
				son[i] = genChars(1)[0];


		return son;
	}

	//function to calculate the fitness
	static public boolean calcFitness(ArrayList<Individual> population){
		boolean stopCount = false;

		for (Individual i : population) {
			if(i.initializated == false){
				//this means i have to calculate its fitness
				//calculate the fitness of the population array and put into fitness
				int f = fastFitness(i.getChromosome());
				i.setFitness(f);
				// set the stop count going out the loop when the fitness of the first is == Target.length
				if(f == TARGET.length()) stopCount = true;
			}
		}

		return stopCount;
	}

	static private int fastFitness(char[] dna){
		int c=0;
		for(int j=0; j<TARGET.length(); j++){
			if(dna[j] == TARGET.charAt(j))
				c++;
		}
		return c;
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