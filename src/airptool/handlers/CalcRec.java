package airptool.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;

public class CalcRec {
	int k;
	HashSet<DadosView> tab;
	
	public CalcRec(){
		this.tab = new HashSet<DadosView>();
		this.tab = CalculaRec();
	}

	public HashSet<DadosView> CalculaRec() {

		HashSet<DadosView> recFinal = new HashSet<DadosView>();
		String temp6, temp7, temp8, temp9, temp10, temp11;
		temp6 = temp7 = temp8 = temp9 = temp10 = temp11 = "";
		
		for (k = 0; k < 3; k++) {

			double fx = 0;
			double[] x = new double[4];
			x[0] = 1;
			x[1] = 1;
			x[2] = 1;
			x[3] = 1;

			String csvFile = "";

			File dir = new File(System.getProperty("user.home") + "/Desktop/temp_view/");
			File[] matches = dir.listFiles(new FilenameFilter() {

				public boolean accept(File dir, String name) {
					if (k == 0) {
						return name.endsWith("CP.csv");
					} else if (k == 1) {
						return name.endsWith("MC.csv");
					} else {
						return name.endsWith("BM.csv");
					}
				}

			});

			for (File arquivo : matches) {
				BufferedReader br = null;
				String line = "";
				String cvsSplitBy = ",";
				double fxMax = 0;
				double fxMin = 0;
				HashSet<DadosView> fxMinRec = new HashSet<DadosView>();
				int countMax = 0, countMin = 0;
				boolean currentIsMax=true, primeiro=true;
				boolean entrouMin = false;

				try {

					br = new BufferedReader(new FileReader(arquivo));
					while ((line = br.readLine()) != null) {

						// use comma as separator
						String[] country = line.split(cvsSplitBy);

						if (!country[0].equals("-")) {
							if(k!=2){
								if (Integer.parseInt(country[0]) == 1 && !primeiro) {
									for(DadosView fmr: fxMinRec){
										if (fmr.getFx() > fxMax) {
											fmr.setFx(fmr.getFx()-fxMax);
											recFinal.add(fmr);
										}
									}
									fxMax = 0;
									fxMin = 0;
									fxMinRec.clear();
								}
								
								else if (Integer.parseInt(country[0]) == 2) {
									primeiro=false;
								}
							}else{
								if (Integer.parseInt(country[0]) == 1 && country[5].equals("Max") && entrouMin) {
									for(DadosView fmr: fxMinRec){
										if (fmr.getFx() > fxMax) {
											fmr.setFx(fmr.getFx()-fxMax);
											recFinal.add(fmr);
										}
									}
									fxMax = 0;
									fxMin = 0;
									fxMinRec.clear();
								}
								
								else if (Integer.parseInt(country[0]) == 1 && country[5].equals("Min")) {
									entrouMin=true;
								}
							}
						}

						if (!country[0].equals("-")) {
								double a,b,c,d;
								String MaxMin;
								if(k==2){
									a = Double.parseDouble(country[1]);
									b = Double.parseDouble(country[2]);
									c = Double.parseDouble(country[3]);
									d = Double.parseDouble(country[4]);
									MaxMin = country[5]; 
								}else{
									a = Double.parseDouble(country[2]);
									b = Double.parseDouble(country[3]);
									c = Double.parseDouble(country[4]);
									d = Double.parseDouble(country[5]);
									MaxMin = country[6];
								}

								if (MaxMin.equals("Max")) {
									if(k==0){
										fxMax = fxMax + ((Math.pow(a, 2)) / (b+a)*(c+a));
									}
									else if (k==1){
										fxMax = fxMax + ((a) / (a + 2*b + 2*c));
									}
									else{
										fxMax = fxMax + ((a) / (a + b + c + d));
									}

									countMax += 1;
									currentIsMax = true;
								} else if (MaxMin.equals("Min")) {
									entrouMin = true;
									if(k==0){
										fxMin = fxMin + ((Math.pow(a, 2)) / (b+a)*(c+a));
										temp7=country[7];
										temp8=country[8];
										temp9=country[9];
									}
									else if (k==1){
										fxMin = fxMin + ((a) / (a + 2*b + 2*c));
										temp7=country[7];
										temp8=country[8];
										temp9=country[9];
										temp10=country[10];
										temp11=country[11];
									}
									else{
										fxMin = fxMin + ((a) / (a + b + c + d));
										temp6=country[6];
										temp7=country[7];
										temp8=country[8];
										temp9=country[9];
									}

									countMin += 1;
									currentIsMax = false;
								}
						} else if (country[0].equals("-")) {
							if (currentIsMax) {
								fxMax = (fxMax / countMax);
								countMax = 0;
							} else {
								fxMin = (fxMin / countMin);
								
								if(k==0){
									fxMinRec.add(new DadosView(fxMin,temp7, temp8, temp9));
								}
								else if(k==1){
									fxMinRec.add(new DadosView(fxMin,temp7, temp8, temp9, temp10, temp11));
								}
								else{
									fxMinRec.add(new DadosView(fxMin,temp6, temp7, temp8, temp9));
								}
								countMin = 0;
								fxMin = 0;
							}
						}

					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return recFinal;
	}
	
	public HashSet<DadosView> getTab(){
		return tab;
	}
}