package com.gendeathrow.pmobs.handlers.random;

import java.util.Random;

import net.minecraft.util.WeightedRandom;

public class AbstractEquipmentWeighted extends WeightedRandom.Item{

	protected Random rand = new Random();
	
	public AbstractEquipmentWeighted(int itemWeightIn) {
		super(itemWeightIn);
	}


	public void createItemStack() {
		
	}
	

}
