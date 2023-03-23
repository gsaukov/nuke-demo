package com.nukedemo.core.batch.inputmodel;

import java.util.List;
import lombok.Data;

@Data
public class BatchInput {
	private List<InputItem> input;
}