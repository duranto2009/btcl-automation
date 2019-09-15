package common.note;

import java.text.DecimalFormat;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
import connection.DatabaseConnection;
import util.SqlGenerator;

/**
 * This class is designed to store different kinds of note/notice that is sent to users.Ex- AdviceNote, 
 * FR Request Note etc. The basic Html of the note will be stored here
 * @author Alam
 */
@TableName("at_common_note")
public class CommonNote {

	@PrimaryKey
	@ColumnName("comntID")
	private long id;
	
	@ColumnName("comntEntityTypeId")
	private long entityTypeId;
	
	@ColumnName("comntEntityId")
	private long entityId;
	
	@ColumnName("comntNoteTypeId")
	private long noteTypeId;
	
	@ColumnName("comntNoteBody")
	private String noteBody = "";

	@ColumnName("comntReqID")
	private long reqID;
	
	@ColumnName("comnLastModificationTime")
	long lastModificationTime;
	
	public CommonNote(){
		lastModificationTime=System.currentTimeMillis();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEntityTypeId() {
		return entityTypeId;
	}

	public void setEntityTypeId(long entityTypeId) {
		this.entityTypeId = entityTypeId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public long getNoteTypeId() {
		return noteTypeId;
	}

	public void setNoteTypeId(long noteTypeId) {
		this.noteTypeId = noteTypeId;
	}

	public String getNoteBody() {
		return noteBody;
	}

	public void setNoteBody(String noteBody) {
		this.noteBody = noteBody;
	}
	
	public void startDescription(String columnName1, String columnName2){
		
		noteBody += "<table class='items'><thead><tr class='title textcenter'><td width='70%'>"+columnName1+"</td><td width='30%'>"+columnName2+"</td></tr></thead><tbody>";		
	}
	
	public void startDescription(String[] columns ){
		noteBody += "<table class='items'><thead><tr class='title textcenter'>";
		
		for( String col: columns ){
			
			noteBody += "<td width='"+(100/columns.length)+"%'>" + col + "</td>";
		}
		noteBody += "</tr></thead><tbody>";
	}
	
	public void addRow( Object ... tableCells ){
		
		noteBody += "<tr>";
		boolean firstColumn = true;
		
		for (Object cell : tableCells) {
			
			noteBody += "<td";
			if( firstColumn ) firstColumn = false;
			else noteBody += " class='textcenter'";
			
			noteBody += ">" + cell + "</td>";
		}
		noteBody += "</tr>";
	}
	
	public void addRow(String label,double value){
		noteBody += "<tr><td>"+label+"</td><td class='textcenter'>"+value+"</td></tr>\n";
	}
	
	public void addRow(String label,String value){
		noteBody += "<tr><td>"+label+"</td><td class='textcenter'>"+value+"</td></tr>\n";
	}
	
	public void addRowRed(String label,String value){
		noteBody += "<tr><td style='color:red'>"+label+"</td><td class='textcenter' style='color:red'>"+value+"</td></tr>\n";
	}
	
	public void endDescription(){
		noteBody +="</tbody></table><br>";
	}

	public long getReqID() {
		return reqID;
	}
	public void setReqID(long reqID) {
		this.reqID = reqID;
	}

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	@Override
	public String toString() {
		return "CommonNote [id=" + id + ", entityTypeId=" + entityTypeId + ", entityId=" + entityId + ", noteTypeId="
				+ noteTypeId + ", noteBody=" + noteBody + ", reqID=" + reqID + ", lastModificationTime="
				+ lastModificationTime + "]";
	}
	
}
