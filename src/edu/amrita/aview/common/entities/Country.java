/*
 * 
 */
package edu.amrita.aview.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;

	
/**
	 * The Class Country.
	 *
	 * @author
	 */
	@Entity
	@Table(name="country")
	public class Country extends Auditable {
			
		/** The country id. */
		private Integer countryId=0;
		
		/** The country name. */
		private String countryName=null;
			
		
		/**
		 * Gets the country id.
		 *
		 * @return the country_id
		 */
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name = "country_id")
		public Integer getCountryId() {
			return countryId;
		}

		/**
		 * Sets the country id.
		 *
		 * @param countryId the new country id
		 */
		public void setCountryId(Integer countryId) {
			this.countryId = countryId;
		}
		
		/**
		 * Gets the country name.
		 *
		 * @return the country_name
		 */
		@Column(name = "country_name")
		public String getCountryName() {
			return countryName;
		}
		
		/**
		 * Sets the country name.
		 *
		 * @param countryName the country_name to set
		 */
		public void setCountryName(String countryName) {
			this.countryName =countryName;
		}
		
		/* (non-Javadoc)
		 * @see com.amrita.edu.entities.Auditable#toString()
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder(256);
			sb.append(this.countryId);
			sb.append(Constant.DELIMETER);
			sb.append(this.countryName);
			sb.append(Constant.DELIMETER);
			sb.append(super.toString());
			sb.append(Constant.DELIMETER);
			return sb.toString();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((countryName == null) ? 0 : countryName.hashCode());
			return result;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Country other = (Country) obj;
			if (countryName == null) {
				if (other.countryName != null)
					return false;
			} else if (!countryName.equals(other.countryName))
				return false;
			return true;
		}



}
