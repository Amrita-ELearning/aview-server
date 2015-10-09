package edu.amrita.aview.gclm.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.entities.Auditable;

/**
 * This is an entity class.
 *
 * @author Vishnupreethi
 * @version 4.0
 * @since 3.0
 */
@Entity
@Table(name = "people_count")
@BatchSize(size=1000)
public class PeopleCount extends Auditable {
	private Long peopleCountId = 0l;
	private Long lectureId = 0l ;
	private Timestamp peopleCountTimestamp = null ;	

	private String peopleCountData = null ;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "people_count_id")
	public Long getPeopleCountId() {
		return peopleCountId;
	}

	public void setPeopleCountId(Long peopleCountId) {
		this.peopleCountId = peopleCountId;
	}

	@Column(name = "lecture_id")
	public Long getLectureId() {
		return lectureId;
	}

	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}
	@Column(name = "people_count_timestamp")
	public Timestamp getPeopleCountTimestamp() {
		return peopleCountTimestamp;
	}
	
	public void setPeopleCountTimestamp(Timestamp peopleCountTimestamp) {
		this.peopleCountTimestamp = peopleCountTimestamp;
	}
	@Column(name = "people_count_data")
	public String getPeopleCountData() {
		return peopleCountData;
	}

	public void setPeopleCountData(String peopleCountData) {
		this.peopleCountData = peopleCountData;
	}

}
