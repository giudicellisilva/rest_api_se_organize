package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_roles")
@Data
public class Role {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public enum Values {
		ADMIN(1L),
		SUBSCRIBER(2L),
		BASIC(3L);
		
		long roleId;
		
		Values(long roleId){
			this.roleId = roleId;
		}
		
		public long getRoleId() {
			return roleId;
		}
		
	}
}
