package com.ibm.webdev.app.wink.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TicketDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="{ticket.id.isempty.error}", groups={Validgroup.update.class})
	private String id;
    @NotEmpty(message="{ticket.schedulId.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private String schedulId;
	private String phone;
	@NotNull(message="{ticket.showtime.isempty.error}", groups={Validgroup.insert.class, Validgroup.update.class})
	private Timestamp bookingtime;

	public TicketDto() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchedulId() {
		return schedulId;
	}

	public void setSchedulId(String schedulId) {
		this.schedulId = schedulId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Timestamp getBookingtime() {
		return bookingtime;
	}

	public void setBookingtime(Timestamp bookingtime) {
		this.bookingtime = bookingtime;
	}

}
