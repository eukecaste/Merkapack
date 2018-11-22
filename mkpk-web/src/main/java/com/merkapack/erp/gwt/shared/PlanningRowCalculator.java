package com.merkapack.erp.gwt.shared;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.merkapack.erp.core.model.Planning;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkPair;

public class PlanningRowCalculator {

	private static Logger LOGGER = Logger.getLogger(PlanningRowCalculator.class.getName());
//	private static final double MINUTES_BREAK = ((16 * 60) - (0.5 * 60));

	public static enum Strategy implements Serializable {
		AMOUNT_CHANGED {
			public void calculate(Planning planning) {
				if (basicCalculate(planning)) {
					LOGGER.info("calculateFromAmount");
					planning.setMeters(MkpkMathUtils
							.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
					planning.setBlows(MkpkMathUtils.round(planning.getAmount() / planning.getBlowUnits()));
					planning.setMinutes(MkpkMathUtils.round(planning.getBlows() / planning.getBlowsMinute()));
				}
			}
		},
		METERS_CHANGED {
			public void calculate(Planning planning) {
				if (basicCalculate(planning)) {
					LOGGER.info("calculateFromMeters");
					double amount = MkpkMathUtils.round((planning.getMeters() * planning.getBlowUnits() * 1000) / planning.getLength());
					double blows = MkpkMathUtils.floor(amount / planning.getBlowUnits(), 0);
					planning.setBlows(blows);
					planning.setAmount( MkpkMathUtils.round(blows * planning.getBlowUnits()) );
					AMOUNT_CHANGED.calculate(planning);
//					planning.setMeters(MkpkMathUtils.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
//					planning.setMinutes(MkpkMathUtils.round(planning.getBlows() / planning.getBlowsMinute()));
				}
			}
		},
		TIME_CHANGED {
			public void calculate(Planning planning) {
				if (basicCalculate(planning)) {
					LOGGER.info("calculateFromTime");
					double blows = MkpkMathUtils.floor(planning.getBlowsMinute() * planning.getMinutes(),0);
					blows = MkpkMathUtils.floor( blows / planning.getBlowUnits(), 0); 
					planning.setBlows( blows );
					planning.setAmount(MkpkMathUtils.round(planning.getBlows() * planning.getBlowUnits()));
					AMOUNT_CHANGED.calculate(planning);
//					planning.setMeters(MkpkMathUtils
//							.round((planning.getLength() * planning.getAmount()) / (1000 * planning.getBlowUnits())));
				}
			}
		};
		public abstract void calculate(Planning planning);

		protected boolean basicCalculate(Planning planning) {
			LOGGER.info("basicCalculate");
			if (planning.getProduct() == null) {
				LOGGER.info("Product null (return false)");
				initialize(planning);
				return false;
			}
			planning.setWidth(planning.getProduct().getWidth());
			planning.setLength(planning.getProduct().getLength());

			if (planning.getMaterial() == null) {
				LOGGER.info("Material null");
				if (planning.getProduct().getMaterial() == null) {
					LOGGER.info("No Material in product (return false)");
					initialize(planning);
					return false;
				}
				LOGGER.info("Material set from product");
				planning.setMaterial(planning.getProduct().getMaterial());
			}
			if (planning.getRoll() != null) {
				LOGGER.info("Roll is null");
				planning.setRollWidth(planning.getRoll().getWidth());
				planning.setRollLength(planning.getRoll().getLength());
				planning.setBlowUnits((int) MkpkMathUtils.round(planning.getRollWidth() / planning.getWidth(), 0));
			}
			return !MkpkMathUtils.isZero(planning.getBlowUnits());
		}

	}

	public static void calculate(Planning planning) {
		calculate(planning, Strategy.AMOUNT_CHANGED);
	}

	public static void calculate(Planning planning, Strategy strategy) {
		strategy.calculate(planning);
	}

	private static void initialize(Planning planning) {
		planning.setWidth(0);
		planning.setLength(0);
		planning.setRollWidth(0);
		planning.setRollLength(0);
		planning.setAmount(0);
		planning.setMeters(0);
		planning.setBlows(0);
		planning.setMinutes(0);
	}
	
	private static boolean shiftList(int i, LinkedList<Planning> list, MkpkPair<Planning,Planning> pair) {
		if (pair != null) {
			list.set(i,pair.getLeft());
			if ( (i+1) >= list.size()) {
				list.add(pair.getRight());
			} else {
				list.add(i+1,pair.getRight());
			}
			return true;
		}
		return false;
	}
	
	public static LinkedList<Planning> calculate(LinkedList<Planning> list) {
		boolean goOn = true;
		while (goOn) {
			goOn = false;
			for (int i = 0; i < list.size(); i++) {
				Planning pl = list.get(i);
				pl.setOrder(i+1);
				if (shiftList(i,list, splitLineMeters(pl))) {
					goOn = true;
					break;
				}
			}
		}
		return list;
	}
	
	public static MkpkPair<Planning,Planning> splitLineMeters(Planning pl) {
		if (pl.getMeters() > pl.getRollLength()) {
			Planning left = pl.clone();
			double rollLength = pl.getRollLength();
			left.setMeters(rollLength);
			calculate(left, Strategy.METERS_CHANGED);
			
			Planning right = pl.clone();
			right.setMeters(MkpkMathUtils.round(pl.getMeters() - left.getMeters()));
			right.setOrder(left.getOrder() + 1);
			calculate(right, Strategy.METERS_CHANGED);
			
			return new MkpkPair<Planning,Planning>(left,right);
		}
		return null;
	}

/*
					if (GWTDateUtils.compare(date, pl.getDate()) != 0) {
						minutes = 0;
						date = pl.getDate();
					}
						
					if (!process && GWTDateUtils.compare(date, pl.getDate()) == 0) {
						minutes = minutes + pl.getMinutes();
						LOGGER.severe("minutes ..: " + minutes);
						if (minutes > MINUTES_BREAK) {
							Planning left = pl.clone();
							Planning right = pl.clone();
							double typedMinutes = left.getMinutes();

							double minutes1 = minutes - MINUTES_BREAK;
							left.setMinutes(minutes1);
							calculate(left, Strategy.TIME_CHANGED);
							ret.add(left);
	
							double minutes2 = typedMinutes - minutes1;
							right.setOrder(right.getOrder() + 1);
							right.setMinutes(minutes2);
							right.setDate(new Date(left.getDate().getTime()));
							right.setDate(GWTDateUtils.addDays(right.getDate(), 1));
							calculate(right, Strategy.TIME_CHANGED);
							LOGGER.severe("Date change..: " + left.getDate());
							ret.add(right);
							process = true;
						}
					}
				}
				if ( !process) {
					ret.add(pl);
				}
			}
 	
	public static LinkedList<Planning> calculateMeters(LinkedList<Planning> list) {
		LinkedList<Planning> ret = new LinkedList<Planning>();
		boolean process = true;

		while (process) {
			int roll = 0;
			process = false;
			for (Planning left : list) {
				if (!process) {
					if (roll == 0) {
						roll = left.getRoll().getId(); 
					}
					if (roll == left.getRoll().getId()) {
						if (left.getMeters() > left.getRollLength()) {
							double typedMeters = left.getMeters();
							Planning right = left.clone();
							left.setMeters(left.getRollLength());
							calculate(left, Strategy.METERS_CHANGED);
							ret.add(left);
	
							double meters1 = typedMeters - left.getRollLength();
							right.setOrder(right.getOrder() + 1);
							right.setMeters(meters1);
							calculate(right, Strategy.METERS_CHANGED);
							LOGGER.severe("Meters change..: " + left.getDate());
							ret.add(right);
							process = true;
						}
					}
				}
				if ( !process) {
					ret.add(left);
				}
			}
		}
		return ret;

	}
 */

}
