package com.preditor.ideb;

import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.JRip;
import weka.classifiers.rules.OneR;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class Preditor {
	private static final Logger log = Logger.getLogger(Preditor.class);
	private static final DecimalFormat formater = new DecimalFormat("#,###.0000");

	private NaiveBayes naiveBayes;
	private J48 j48;
	private OneR oneR;
	private JRip jRip;
	private IBk iBk;
	private LibSVM svm;
	private MultilayerPerceptron multPerceptron;
	
	private boolean naiveBayesBuilded;
	private boolean j48Builded;
	private boolean oneRBuilded;
	private boolean jRipBuilded;
	private boolean iBkBuilded;
	private boolean svmBuilded;
	private boolean multPerceptronBuilded;
	
	private Instances instancies;
	
	public Preditor() {
		naiveBayes = new NaiveBayes();
		j48 = new J48();
		oneR = new OneR();
		jRip = new JRip();
		iBk = new IBk();
		svm = new LibSVM();
		multPerceptron = new MultilayerPerceptron();

        svm.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_LINEAR, LibSVM.TAGS_KERNELTYPE));
	}
	
	public void loadWekaData(String fileLocation) throws Exception {
		DataSource ds = new DataSource(fileLocation);
		
		instancies = ds.getDataSet();
		instancies.setClassIndex(instancies.numAttributes() - 1);
		log.info("Total of instances loaded: " + instancies.numInstances());
		log.info("Total of attributes: " + instancies.numAttributes());
		
		naiveBayesBuilded = false;
		j48Builded = false;
		oneRBuilded = false;
		jRipBuilded = false;
		iBkBuilded = false;
		svmBuilded = false;
		multPerceptronBuilded = false;
	}
	
	public String buildClassifier(ClassifierType type) throws Exception {
		if (instancies == null)
			return "Nenhum dado encontrado para classificar";
		
		switch (type) {
		case NAIVE_BAYES:
			naiveBayes.buildClassifier(instancies);
			break;
		case J48:
			j48.buildClassifier(instancies);
			break;
		case IBK:
			iBk.buildClassifier(instancies);
			break;
		case J_RIP:
			jRip.buildClassifier(instancies);
			break;
		case ONE_R:
			oneR.buildClassifier(instancies);
			break;
		case SVM:
			svm.buildClassifier(instancies);
			break;
		case MULT_LAYER_PERCEPTRON:
			multPerceptron.buildClassifier(instancies);
			break;
		default:
			throw new Exception("No classifier was given");
		}
		return null;
	}
	
	public void buildAllClassifier() throws Exception {
		naiveBayes.buildClassifier(instancies);
		j48.buildClassifier(instancies);
		iBk.buildClassifier(instancies);
		jRip.buildClassifier(instancies);
		oneR.buildClassifier(instancies);
		svm.buildClassifier(instancies);
		multPerceptron.buildClassifier(instancies);
	}

	public Average classify(ClassifierType type, String data) throws Exception {
		return classify(type, processInstancies(data));
	}
	
	public Average classify(ClassifierType type, File file) throws Exception {
		return classify(type, processInstancies(file));
	}
	
	public Average classify(ClassifierType type, Instance newInstancies) throws Exception {
		double[] result;
		switch (type) {
		case NAIVE_BAYES:
			if (!naiveBayesBuilded) {
				buildClassifier(ClassifierType.NAIVE_BAYES);
				naiveBayesBuilded = true;
			}
			result = naiveBayes.distributionForInstance(newInstancies);
			break;
		case J48:
			if (!j48Builded) {
				buildClassifier(ClassifierType.J48);
				j48Builded = true;
			}
			result = j48.distributionForInstance(newInstancies);
			break;
		case IBK:
			if (!iBkBuilded) {
				buildClassifier(ClassifierType.IBK);
				iBkBuilded = true;
			}
			result = iBk.distributionForInstance(newInstancies);
			break;
		case J_RIP:
			if (!jRipBuilded) {
				buildClassifier(ClassifierType.J_RIP);
				jRipBuilded = true;
			}
			result = jRip.distributionForInstance(newInstancies);
			break;
		case ONE_R:
			if (!oneRBuilded) {
				buildClassifier(ClassifierType.ONE_R);
				oneRBuilded = true;
			}
			result = oneR.distributionForInstance(newInstancies);
			break;
		case SVM:
			if (!svmBuilded) {
				buildClassifier(ClassifierType.SVM);
				svmBuilded = true;
			}
			result = svm.distributionForInstance(newInstancies);
			break;
		case MULT_LAYER_PERCEPTRON:
			if (!multPerceptronBuilded) {
				buildClassifier(ClassifierType.MULT_LAYER_PERCEPTRON);
				multPerceptronBuilded = true;
			}
			result = multPerceptron.distributionForInstance(newInstancies);
			break;
		default:
			throw new Exception("No classifier was given");
		}
		return new Average(result[0], result[1]);
	}

	public Averages classifyAll(String data) throws Exception {
		return classifyAll(processInstancies(data));
	}
	
	public Averages classifyAll(File file) throws Exception {
		return classifyAll(processInstancies(file));
	}
	
	public Averages classifyAll(Instance newInstancies) throws Exception {
		return new Averages(
				classify(ClassifierType.NAIVE_BAYES, newInstancies),
				classify(ClassifierType.J48, newInstancies),
				classify(ClassifierType.ONE_R, newInstancies),
				classify(ClassifierType.J_RIP, newInstancies),
				classify(ClassifierType.IBK, newInstancies),
				classify(ClassifierType.SVM, newInstancies),
				classify(ClassifierType.MULT_LAYER_PERCEPTRON, newInstancies));
	}
	
	private Instance processInstancies(String data) {
		Instance newInstancies = new DenseInstance(instancies.numAttributes());
		newInstancies.setDataset(instancies);
		processInstancies(newInstancies, data);
		return newInstancies;
	}
	
	private Instance processInstancies(File file) {
		Instance newInstancies = new DenseInstance(instancies.numAttributes());
		newInstancies.setDataset(instancies);
		// TODO: Implement
		return newInstancies;
	}
	
	private void processInstancies(Instance newInstancies, String data) {
		data = data.replaceAll(" ", "");
		for (int i=0; i < data.length(); i++) {
			newInstancies.setValue(i, data.charAt(i));
		}
	}
	
	public class Average {
		private double below;
		private double above;
		
		public Average (double below, double above) {
			this.below = below;
			this.above = above;
		}
		
		public double belowValue() {
			return below;
		}
		
		public double aboveValue() {
			return above;
		}

		public String formatedBelow() {
			return formater.format(this.below);
		}
		
		public String formatedAbove() {
			return formater.format(this.above);
		}
	
		@Override
		public String toString() {
			return "AVG<below: " + formatedBelow() + ", above: " + formatedAbove() + ">";
		}
	}
	
	public class Averages {
		private Average naiveBayes;
		private Average j48;
		private Average oneR;
		private Average jRip;
		private Average iBk;
		private Average svm;
		private Average multPerceptron;
		
		public Averages(Average naiveBayes, Average j48, Average oneR, Average jRip, Average iBk, Average svm,
				Average multPerceptron) {
			this.naiveBayes = naiveBayes;
			this.j48 = j48;
			this.oneR = oneR;
			this.jRip = jRip;
			this.iBk = iBk;
			this.svm = svm;
			this.multPerceptron = multPerceptron;
		}
		
		public double belowValue(ClassifierType type) throws Exception {
			return getAverage(type).belowValue();
		}
		
		public double aboveValue(ClassifierType type) throws Exception {
			return getAverage(type).aboveValue();
		}

		public String formatedBelow(ClassifierType type) throws Exception {
			return getAverage(type).formatedBelow();
		}

		public String formatedAbove(ClassifierType type) throws Exception {
			return getAverage(type).formatedAbove();
		}
		
		public Average getAverage(ClassifierType type) throws Exception{
			switch (type) {
			case NAIVE_BAYES:
				return this.naiveBayes;
			case J48:
				return this.j48;
			case IBK:
				return this.iBk;
			case J_RIP:
				return this.jRip;
			case ONE_R:
				return this.oneR;
			case SVM:
				return this.svm;
			case MULT_LAYER_PERCEPTRON:
				return this.multPerceptron;
			default:
				throw new Exception("No classifier was given");
			}
		}
		
		public Average getNaiveBayes() {
			return naiveBayes;
		}

		public Average getJ48() {
			return j48;
		}

		public Average getOneR() {
			return oneR;
		}

		public Average getjRip() {
			return jRip;
		}

		public Average getiBk() {
			return iBk;
		}

		public Average getSvm() {
			return svm;
		}

		public Average getMultPerceptron() {
			return multPerceptron;
		}
	}
}
