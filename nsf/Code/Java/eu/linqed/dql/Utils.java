package eu.linqed.dql;

import java.io.File;

import com.ibm.xsp.extlib.util.ExtLibUtil;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;

abstract class Utils {
	
	public static final String FAKENAMES = "fakenames.nsf";
	
	//get demo data database (in the same folder as this one)
	public static Database getFakenamesDb() throws NotesException {
		Session s = ExtLibUtil.getCurrentSession();
		String path = s.getCurrentDatabase().getFilePath();
		if (path.indexOf(File.separator) >-1) {
			path = path.substring(0, path.lastIndexOf(File.separator)) + File.separator;
		} else {
			path = "";
		}
		return s.getDatabase("", path + FAKENAMES);
	}


	// update a counter to see how often the button is clicked
	public static void logCount() {

		try {
			Document doc = ExtLibUtil.getCurrentDatabase().getView("count").getFirstDocument();

			int count = 0;

			if (doc == null) {
				doc = ExtLibUtil.getCurrentDatabase().createDocument();
				doc.replaceItemValue("form", "counter");
			} else {
				count = doc.getItemValueInteger("numQueries");
			}

			count++;

			doc.replaceItemValue("numQueries", count);
			doc.save();
		} catch (NotesException e) {
			e.printStackTrace();
		}

	}

}
