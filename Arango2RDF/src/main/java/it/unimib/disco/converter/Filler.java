package it.unimib.disco.converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.hp.hpl.jena.rdf.model.Model;

public abstract class Filler {

	protected Map<Integer, Integer> map;
	protected Map<Integer, Integer> maForLog;
	protected String log_file;
	protected int pageSize;
	protected int count;
	protected Model model;
	protected String type;
	
	public void initialize(String log_file, int pageSize, int count, String type) {
		this.log_file = log_file;
		this.pageSize = pageSize;
		this.count = count;
		this.type = type;
		map = new TreeMap<Integer, Integer>();
		maForLog = new TreeMap<Integer, Integer>();
	}
	
	public abstract void init_ontology();
	
	public void readLog() throws Exception {
		File log = new File(log_file);
		if (log.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(log));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split("-");
				map.put( Integer.valueOf(splitted[0]), Integer.valueOf(splitted[1]));
				maForLog.put( Integer.valueOf(splitted[0]), Integer.valueOf(splitted[1]));
			}
			br.close();
		}
	}
	
	
	public void find_holes() throws Exception{
		Iterator<Integer> it = map.keySet().iterator();
		if (!map.keySet().isEmpty()) {
			int firstDoc = it.next();
			if (firstDoc != 1)
				fill_hole(1, firstDoc -1, false);
		}
		it = map.keySet().iterator();

		int common = 0;
		boolean first = true;
		
		for (int i = 0; i < map.keySet().size() -1; i++) {
			int hole_start;
			if (first) {
				hole_start = map.get(it.next()) + 1;
				first = false;
			} 
			else
				hole_start = map.get(common) + 1;
			common = it.next();
			int hole_end = common - 1;
	
			fill_hole(hole_start, hole_end, false);
		}
		
		// filling the last hole or the only (the full collection)
		if(map.keySet().size() == 0)
			fill_hole(1, (int)count, true);
		else if(map.keySet().size() == 1)
			fill_hole(map.get(it.next()) + 1, (int)count, true);
		else
			fill_hole(map.get(common) + 1, (int)count, true);
	}
	
	
	public void fill_hole(int hole_start, int hole_end, boolean isLastHole) throws Exception { 
		if(hole_end - hole_start +1 >= pageSize || isLastHole) {
			int offset = 0;
			while (hole_end - (hole_start + offset) + 1 >= pageSize) {
				if ((hole_start + offset + pageSize - 1) % pageSize == 0) { // if exist a page compatible with the hole and whose first element is  hole_start + offset
					int page = ((hole_start + offset-1) / pageSize); 
					if (prepareRequest(page, hole_start + offset))  
						log(hole_start + offset);
					offset += pageSize;
				}
				else
					offset++;
			}
			if (isLastHole && hole_end - (hole_start + offset) +1 != 0) {
				int page = ((hole_start + offset-1) / pageSize) ;
				if (prepareRequest(page, hole_start + offset))
					log(hole_start + offset);
			}
		}	
	}
	
	
	public void log(int start) throws Exception{
		FileOutputStream fos = new FileOutputStream(this.log_file);
		int end = start + pageSize -1;
		if(end > count)
			end = (int) count;
		int toRemove1 = -1;
		int toRemove2 = -1;
		
		for(int key : maForLog.keySet()) {
			if(maForLog.get(key) +1 == start) 
				toRemove1 = key;
			if(key -1 == end) 
				toRemove2 = key;
		}
		
		if (toRemove1 != -1) {
			maForLog.put(toRemove1, end);
			if (toRemove2 != -1) {
				maForLog.put(toRemove1, maForLog.get(toRemove2));
				maForLog.remove(toRemove2);
			}
		} 
		else {
			if (toRemove2 != -1) {
				maForLog.put(start, maForLog.get(toRemove2));
				maForLog.remove(toRemove2);
			}
			else                                   
				maForLog.put(start, end);
		}
		
		for(int key : maForLog.keySet()) 
			fos.write((key + "-" + maForLog.get(key) + "\n").getBytes());	
		fos.close();
		
		System.out.print(". Logged");
	}
	
	
	public boolean prepareRequest(int page, int start) {
		init_ontology();
		int end = start + pageSize - 1;
		if (end > count)
			end = (int) count;
		System.out.print("\nRequesting "+ type + " [" + start + "," + end + "] page: " + page + " pageSize: " + pageSize);
		try {
			request(page, pageSize);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	public abstract void request( int nPage, int pageSize) throws Exception ;
	
}
