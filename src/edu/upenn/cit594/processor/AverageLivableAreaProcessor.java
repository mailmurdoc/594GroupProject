package edu.upenn.cit594.processor;

import edu.upenn.cit594.data.OverallData;
import edu.upenn.cit594.data.ZipCodeData;

public class AverageLivableAreaProcessor implements AverageProcessor{
	
	/*
	 * method to get average for livable area
	 */
	@Override
	public double getAverage(String fileName, int zip, int selectedOption) {
		ZipCodeData zipData = OverallData.zipCodeMap.get(zip);
		if(zipData == null) {
			rp.readProperties(selectedOption, fileName, zip);
			zipData.totalLivableArea = zipProcessor.totalLivableAreas(zip);
			zipData.averageLivableArea = zipProcessor.averageValue(zipData.totalLivableArea, 
					zipData.households);
		}
		if (zipData.averageLivableArea == 0) {
			if (zipData.totalLivableArea == 0) {
				if (zipData.livableArea == null) {
					rp.readProperties(selectedOption, fileName, zip);
				}	
				zipData.totalLivableArea = zipProcessor.totalMarketValue(zip);
			}

			zipData.averageLivableArea = zipProcessor.averageValue(zipData.totalLivableArea, 
					zipData.households);
		}
		
		return zipData.averageMarketValue;
	}

}
