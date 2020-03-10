package eu.linqed.dql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.DominoQuery;
import lotus.domino.NotesException;

public class DQLExplorer implements Serializable {

	private static final long serialVersionUID = 1L;

	private String query;
	private String explainOutput;
	private String error;
	private boolean explain;

	private List<HashMap<String, Object>> searchResults = new ArrayList<HashMap<String, Object>>();

	public DQLExplorer() {
		// default query
		this.query = "city in ('Arnhem', 'Amsterdam' )";
	}

	public void searchExplain() {
		try {

			// reset
			this.error = null;
			this.explainOutput = null;
			this.searchResults.clear();

			// create query object
			DominoQuery domQuery = getDomQuery();

			// get explain text
			if (this.explain) {
				this.explainOutput = domQuery.explain(this.query).trim();
			}

			// execute query
			this.search(domQuery);

			Utils.logCount();

		} catch (NotesException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		}

	}

	private void search(DominoQuery domQuery) throws NotesException {
		DocumentCollection res = domQuery.execute(this.query);

		this.searchResults.clear();

		Document doc = res.getFirstDocument();
		while (null != doc) {

			HashMap<String, Object> result = new HashMap<String, Object>();
			
			result.put("firstName", doc.getItemValueString("firstName"));
			result.put("lastName", doc.getItemValueString("lastName"));
			result.put("zip", doc.getItemValueString("zip"));
			result.put("city", doc.getItemValueString("city"));
			result.put("email", doc.getItemValueString("email"));
			result.put("language", doc.getItemValue("language"));

			searchResults.add(result);

			Document next = res.getNextDocument();
			doc.recycle();
			doc = next;
		}

	}

	private static DominoQuery getDomQuery() throws NotesException {
		Database dbFake = Utils.getFakenamesDb();
		DominoQuery domQuery = dbFake.createDominoQuery();
		return domQuery;
	}

	public List<HashMap<String, Object>> getSearchResults() {
		return searchResults;
	}
	
	// read the current indexes
	public void listIndex() throws NotesException {
		this.searchResults.clear();
		this.explainOutput = getDomQuery().listIndexes();
	}

	// update the DQL indexes
	public void updateIndex() throws NotesException {
		this.searchResults.clear();
		getDomQuery().setRebuildDesignCatalog(true);
		explainOutput = "Done updating indexes";
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getExplainOutput() {
		return explainOutput;
	}

	public int getNumResults() {
		return this.searchResults.size();
	}

	public String getError() {
		return error;
	}

	public boolean isExplain() {
		return explain;
	}

	public void setExplain(boolean explain) {
		this.explain = explain;
	}

}
