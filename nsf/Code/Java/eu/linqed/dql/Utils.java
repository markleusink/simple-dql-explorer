package eu.linqed.dql;

import com.ibm.xsp.extlib.util.ExtLibUtil;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NotesException;

abstract class Utils {
	
	public static final String FAKENAMES_PATH = "dql/fakenames.nsf";
	
	//get demo data database
	public static Database getFakenamesDb() throws NotesException {
		return ExtLibUtil.getCurrentSession().getDatabase("", FAKENAMES_PATH);
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
