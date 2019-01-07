package com.merkapack.erp.gwt.shared;

import java.util.Date;
import java.util.LinkedList;

import com.merkapack.erp.core.model.Planning;
import com.merkapack.erp.gwt.client.util.GWTDateUtils;
import com.merkapack.watson.util.MkpkMathUtils;
import com.merkapack.watson.util.MkpkPair;

public class PlanningCalculator {

	//private static Logger LOGGER = Logger.getLogger(PlanningRowCalculator.class.getName());
	
	public static void calculate(PlanningCalculatorParams params, Planning planning) {
		PlanningCalculatorStrategy.AMOUNT_CHANGED.calculate(params,planning);
	}
	public static void calculate(PlanningCalculatorParams params, PlanningCalculatorStrategy strategy, Planning planning) {
		strategy.calculate(params,planning);
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
	
	public static LinkedList<Planning> calculate(PlanningCalculatorParams params, LinkedList<Planning> list) {
		boolean goOn = true;
		while (goOn) {
			goOn = false;
			for (int i = 0; i < list.size(); i++) {
				Planning pl = list.get(i);
				pl.setOrder(i+1);
				// Se dividen las líneas cuyos metros son mayores que los de la bobina.
				goOn = (shiftList(i,list, splitLineMeters(params, pl)));
				// Se dividen las líneas cuyos metros son mayores que los de la bobina.
				goOn = !goOn && (shiftList(i,list, splitLineTime(params, pl)));
				if (goOn) break;
			}
		}
		// Se dividen las líneas cuyos acumulado de horas supera la jornada laboral.
		splitGroupTime(params, list);
		return list;
	}
	
	public static MkpkPair<Planning,Planning> splitLineMeters(PlanningCalculatorParams params,Planning pl) {
		if (pl.getMeters() > pl.getRollUpLength()) {
			Planning left = pl.clone();
			double rollLength = pl.getRollUpLength();
			left.setMeters(rollLength);
			calculate(params, PlanningCalculatorStrategy.METERS_CHANGED, left);
			
			Planning right = pl.clone();
			right.setMeters(MkpkMathUtils.round(pl.getMeters() - left.getMeters()));
			right.setOrder(left.getOrder() + 1);
			calculate(params, PlanningCalculatorStrategy.METERS_CHANGED, right);
			
			return new MkpkPair<Planning,Planning>(left,right);
		}
		return null;
	}

	public static MkpkPair<Planning,Planning> splitLineTime(PlanningCalculatorParams params,Planning pl) {
		if (pl.getMinutes() >= params.getMinutesBreak() ) {
			Planning left = pl.clone();
			left.setMinutes(params.getMinutesBreak());
			calculate(params, PlanningCalculatorStrategy.TIME_CHANGED, left);
			
			Planning right = pl.clone();
			right.setMinutes(MkpkMathUtils.round(pl.getMinutes() - left.getMinutes()));
			right.setOrder(left.getOrder() + 1);
			right.setDate(new Date(left.getDate().getTime()));
			right.setDate(GWTDateUtils.addDays(right.getDate(), 1));
			calculate(params, PlanningCalculatorStrategy.TIME_CHANGED, right);
			
			return new MkpkPair<Planning,Planning>(left,right);
		}
		return null;
	}
	
	public static void splitGroupTime(PlanningCalculatorParams params, LinkedList<Planning> list) {
		if (list != null && !list.isEmpty()) {

			double minutes = 0;
			Date date = new Date(list.get(0).getDate().getTime());
			for (int i = 0; i < list.size(); i++) {
				Planning pl = list.get(i);
				if (GWTDateUtils.compare(pl.getDate(), date) < 0) {
					pl.setDate(new Date(date.getTime()));
				}
				if (GWTDateUtils.compare(pl.getDate(), date) <= 0) {
					minutes = minutes + pl.getMinutes();
				}
				if (minutes >= params.getMinutesBreak()) {
					double typedMinutes = pl.getMinutes() - 0;
					Planning left = pl.clone();
					Planning right = pl.clone();

					double minutes1 = MkpkMathUtils.round(typedMinutes - (minutes - params.getMinutesBreak()));
					left.setMinutes(minutes1);
					calculate(params, PlanningCalculatorStrategy.TIME_CHANGED, left);

					double minutes2 = MkpkMathUtils.round(typedMinutes - minutes1);
					right.setOrder(right.getOrder() + 1);
					right.setMinutes(minutes2);
					right.setDate(new Date(left.getDate().getTime()));
					right.setDate(GWTDateUtils.addDays(right.getDate(), 1));
					calculate(params, PlanningCalculatorStrategy.TIME_CHANGED, right);
					shiftList(i, list, new MkpkPair<Planning, Planning>(left, right));
					++i;
					minutes = right.getMinutes();
					date = new Date(right.getDate().getTime());
				}
			}
		}
	}
}
