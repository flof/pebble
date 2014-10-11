/*******************************************************************************
 * This file is part of Pebble.
 * 
 * Copyright (c) 2014 by Mitchell Bösecke
 * 
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.mitchellbosecke.pebble.node.expression;

import java.util.Map;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.node.ArgumentsNode;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

public class FilterExpression extends BinaryExpression<Object> {

	@Override
	public Object evaluate(PebbleTemplateImpl self, EvaluationContext context)
			throws PebbleException {

		FilterInvocationExpression filterInvocation = (FilterInvocationExpression) getRightExpression();
		ArgumentsNode args = filterInvocation.getArgs();
		String filterName = filterInvocation.getFilterName();

		Map<String, Filter> filters = context.getFilters();
		Filter filter = filters.get(filterInvocation.getFilterName());

		if (filter == null) {
			throw new PebbleException(null, String.format(
					"Filter [%s] does not exist.", filterName));
		}

		Map<String, Object> namedArguments = args.getArgumentMap(self, context,
				filter);

		return filter.apply(getLeftExpression().evaluate(self, context),
				namedArguments);
	}
}
