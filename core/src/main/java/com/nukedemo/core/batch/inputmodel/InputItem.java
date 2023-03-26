package com.nukedemo.core.batch.inputmodel;

import lombok.Data;

import java.io.Serializable;

@Data
public class InputItem implements Serializable {
	private String code;
	private String name;
}