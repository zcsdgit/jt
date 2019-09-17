package com.jt.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain=true)
public class BasePojo implements Serializable{
	private Date created;
	private Date updated;
}
