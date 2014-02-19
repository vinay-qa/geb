/* Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package geb.content

import geb.error.UndefinedPageContentException
import geb.error.UnresolvablePropertyException
import geb.navigator.Navigator
import geb.navigator.factory.NavigatorFactory
import org.openqa.selenium.WebElement

/**
 * Designed to be used with the “Delegate” transform to gain all the dollar methods etc.
 */
class NavigableSupport implements Navigable {

	private final owner
	private final Map<String, PageContentTemplate> contentTemplates
	private final NavigatorFactory navigatorFactory

	NavigableSupport(owner, Map<String, PageContentTemplate> contentTemplates, NavigatorFactory navigatorFactory) {
		this.owner = owner
		this.contentTemplates = contentTemplates ?: [:]
		this.navigatorFactory = navigatorFactory
	}

	protected Navigator getNavigator() {
		navigatorFactory.base
	}

	private getContent(String name, Object[] args) {
		def contentTemplate = contentTemplates[name]
		if (contentTemplate) {
			contentTemplate.get(* args)
		} else {
			throw new UndefinedPageContentException(this, name)
		}
	}

	Navigator find() {
		getNavigator()
	}

	Navigator $() {
		getNavigator()
	}

	Navigator find(int index) {
		getNavigator()[index]
	}

	Navigator find(Range<Integer> range) {
		getNavigator()[range]
	}

	Navigator $(int index) {
		getNavigator()[index]
	}

	Navigator $(Range<Integer> range) {
		getNavigator()[range]
	}

	Navigator find(String selector) {
		getNavigator().find(selector)
	}

	Navigator $(String selector) {
		getNavigator().find(selector)
	}

	Navigator find(String selector, int index) {
		getNavigator().find(selector, index)
	}

	Navigator find(String selector, Range<Integer> range) {
		getNavigator().find(selector, range)
	}

	Navigator $(String selector, int index) {
		getNavigator().find(selector, index)
	}

	Navigator $(String selector, Range<Integer> range) {
		getNavigator().find(selector, range)
	}

	Navigator find(Map<String, Object> attributes) {
		getNavigator().find(attributes)
	}

	Navigator $(Map<String, Object> attributes) {
		getNavigator().find(attributes)
	}

	Navigator find(Map<String, Object> attributes, int index) {
		getNavigator().find(attributes, index)
	}

	Navigator find(Map<String, Object> attributes, Range<Integer> range) {
		getNavigator().find(attributes, range)
	}

	Navigator $(Map<String, Object> attributes, int index) {
		getNavigator().find(attributes, index)
	}

	Navigator $(Map<String, Object> attributes, Range<Integer> range) {
		getNavigator().find(attributes, null, range)
	}

	Navigator find(Map<String, Object> attributes, String selector) {
		getNavigator().find(attributes, selector)
	}

	Navigator $(Map<String, Object> attributes, String selector) {
		getNavigator().find(attributes, selector)
	}

	Navigator find(Map<String, Object> attributes, String selector, int index) {
		getNavigator().find(attributes, selector, index)
	}

	Navigator find(Map<String, Object> attributes, String selector, Range<Integer> range) {
		getNavigator().find(attributes, selector, range)
	}

	Navigator $(Map<String, Object> attributes, String selector, int index) {
		getNavigator().find(attributes, selector, index)
	}

	Navigator $(Map<String, Object> attributes, String selector, Range<Integer> range) {
		getNavigator().find(attributes, selector, range)
	}

	Navigator $(Navigator[] navigators) {
		navigatorFactory.createFromNavigators(Arrays.asList(navigators))
	}

	Navigator $(SimplePageContent[] contents) {
		$(contents*.find() as Navigator[])
	}

	Navigator $(WebElement[] elements) {
		navigatorFactory.createFromWebElements(Arrays.asList(elements))
	}

	def methodMissing(String name, args) {
		try {
			getContent(name, * args)
		} catch (UndefinedPageContentException e1) {
			getNavigator()."$name"(* args)
		}
	}

	def propertyMissing(String name) {
		try {
			getContent(name)
		} catch (UndefinedPageContentException e1) {
			try {
				getNavigator()."$name"
			} catch (MissingPropertyException e2) {
				throw new UnresolvablePropertyException(owner, name, "Unable to resolve $name as content for ${owner}, or as a property on its Navigator context. Is $name a class you forgot to import?")
			}
		}
	}

	def propertyMissing(String name, val) {
		try {
			getContent(name).value(val)
		} catch (UndefinedPageContentException e) {
			try {
				getNavigator()."$name" = val
			} catch (MissingPropertyException e1) {
				throw new UnresolvablePropertyException(owner, name, "Unable to resolve $name as a property to set on ${owner}'s Navigator context")
			}
		}
	}
}