# Marky Mark

Marky Mark is a parser that converts markdown into native views. The way it looks is highly customizable and the supported markdown syntax is easy to extend.

## Usage

Override MarkyMark styles where nescessary.

```xml
<!-- Base application theme. -->
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>

    <item name="MarkyMarkTheme">@style/MarkdownStyle</item>  
 </style>
    
 <!-- Theme used by MarkyMark -->
 <style name="MarkdownStyle" parent="MarkyMarkStyle">
    <item name="android:lineSpacingExtra">4dp</item>
    <item name="android:lineSpacingMultiplier">1</item>
    <item name="MarkDownHeader4Style">@style/Header4</item>
</style>

<!-- Different color for H4 tags -->
<style name="Header4" parent="MarkyMarkHeader4">
    <item name="android:textColor">#52c222</item>
</style>
```

### All parent styles

- `MarkyMarkCode`
- `MarkyMarkHeader`
- `MarkyMarkHeader1`
- `MarkyMarkHeader2`
- `MarkyMarkHeader3`
- `MarkyMarkHeader4`
- `MarkyMarkHeader5`
- `MarkyMarkHeader6`
- `MarkyMarkHeaderHorizontalRule` // horizontal lines
- `MarkyMarkImage`
- `MarkyMarkList`
- `MarkyMarkParagraph`
- `MarkyMarkQuote`

Next use MarkyMark to parse Markdown and add the resulting
list of `View`s to a layout.

```java
LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
final MarkyMark<View> markyMark = MarkyMarkAndroid.getMarkyMark(this, new ContentfulFlavor());
List<View> views = markyMark.parseMarkDown("# Header\nParagraph etc");
for (View view : views) {
	layout.addView(view);
}
```

If you want more customization it is also possible to provide your own custom inline/block items, or replace existing ones. More on how you can use these in **Advanced Usage** found below

```java
ThemedContext themedContext = new ThemedContext(activity);

List<DisplayItems> displayItems = new ArrayList<>();
List<InlineDisplayItems> inlineDisplayItems = new ArrayList<>();
// These are some of the default ones, but you get the point
displayItems.add(new HeaderDisplayItem(themedContext));
displayItems.add(new ParagraphDisplayItem(themedContext));
inlineDisplayItems.add(new BoldInlineDisplayItem());

final MarkyMark<View> markyMark = MarkyMarkAndroid.getMarkyMark(
        this, 
        new ContentfulFlavor(), 
        displayItems, 
        inlineDisplayItems
);
```

## Supported tags in Contentful Flavour

```
# Headers
---

# H1
## H2
### H3
#### H4
##### H5
###### H6
### __*bold & italic*__

# Lists
---

## Ordered list
1. Number 1
2. Number 2
3. Number 3
5. Number 4
  1. Nested 1
  2. Nested 2
6. Number 5 click [here](https://m2mobi.com)
7. Number 5

## Unordered list
- Item 1
- Item 2
- Item 3
  - Nested item 1
  - Nested item 2
    - Sub-nested item 1
    - Sub-nested item 2
      - Sub-sub-nested item 1
      - Sub-sub-nested item 2
       - Sub-sub-nested item 3 (with single space) and a very long piece of text

## Combo

1. Ordered 1
2. Ordered 2
- Unordered 1
- Unordered 2
  - __*Nested unordered 1 bold*__
  - Nested unordered 2
    1. Sub-nested ordered 1
    2. Sub-nested ordered 2
    - unordered
    - unordered

# Paragraphs

---
## Quotes
> MarkDown is *awesome*
> Seriously..

## Links
[This is a test link](https://m2mobi.com)
Inline links are also possible, click [here](https://m2mobi.com)
Phone numbers as well [+06-12345678](tel:06-12345678)

## Code

`inline code`
```code block```
```

## Styled text
This is __bold__, this is *italic*, this is ~~striked out~~, this is everything __~~*combined*~~__.
Special html symbols: `&euro; &copy;` become -> &euro; &copy;

## Images
---

`![Alternate text](www.imageurl.com)`

# Advanced usage
---

## Adding your own rules

As of now only a `ContentfulFlavor` is included, however it is entirely possible to create your own Markdown flavor
and/or corresponding rules.

Adding a rule requires these steps

### Extend the marker interface `MarkDownItem`

Create a `MarkDownItem` from which you can create a `View` later, so you'll want to have every piece of information needed in order to create said `View`

```java
public class NewMarkDownItem implements MarkDownItem {

	private String content;

	public NewMarkDownItem(String content) {
	    this.content = content;
	}

	public String getContent() {
		return content;
	}
}
```

### Extend `DisplayItem<View, NewMarkDownItem, Spanned>`

Create a `DisplayItem` that can handle your `NewMarkDownItem` and convert it into a `View`

```java
public class NewDisplayItem implements DisplayItem<View, NewMarkDownItem, Spanned> {

	/** Context used to create Views */
	protected final Context mContext;

	public HeaderDisplayItem(final Context pContext) {
		mContext = pContext;
	}

	@Override
	public View create(final NewMarkDownItem pMarkDownItem, final InlineConverter<Spanned> pInlineConverter) {
		TextView textView = new TextView(mContext);
		textView.setText(pInlineConverter.convert(pMarkDownItem.getContent());
		return textView;
	}
}
```

Now add this item to your `ViewConverter` before you initialize `MarkyMark` like shown at the top of this `README`

```java
viewConverter.addMapping(new NewDisplayItem(themedContext));
```

### Extend `Rule`

Create a `Rule` that recognizes your new item and creates a corresponding `MarkDownItem` for it.
Most new rules will just be single line, like headers, in that case your new rule can just extend `RegexRule`.
Return your regular expression `Pattern` in the `getRegex()` method and return your new `MarkDownItem` in the `toMarkDownItem(final List<String> pMarkDownLines)`

```java
public class NewRule extends RegexRule {

	private static final Pattern PATTERN = Pattern.compile("some regex");

	@Override
	protected Pattern getRegex() {
		return PATTERN;
	}

	@Override
	public MarkDownItem toMarkDownItem(final List<String> pMarkDownLines) {
	    // In this case, since it is a single line rule
	    // pMarkDownLines will always be an array with one String
	    return NewMarkDownItem(pMarkDownLines.get(0));
	}
}
```

### Adding the new rule to `MarkyMark`

You can add a new rule to your `MarkyMark` instance like this

```java
// adding rule to MarkyMark instance
markyMark.addRule(new NewRule());
```

Or create a new `Flavor` altogether

```java
public class OtherFlavor implements Flavor {

	@Override
	public List<Rule> getRules() {
		List<Rule> rules = new ArrayList<>();
		// add all the rules
		return rules;
	}

	@Override
	public List<InlineRule> getInlineRules() {
		List<InlineRule> rules = new ArrayList<>();
		// Add all the rules
		return rules;
	}

	@Override
	public Rule getDefaultRule() {
		return new NewDefaultRule();
	}
}
```

And pass it in the `MarkyMark.Builder()`

```java
new MarkyMark.Builder<View>().addFlavor(new OtherFlavor()) // etc
```

### Multi line blocks

For more complicated `Rules` that can detect multi-line blocks you'll want to extend `Rule` and you have override the `conforms(final List<String> pMarkDownLines)` method where you would return `true` if the **first** line is recognized as the start of your block, and false otherwise. However there is a catch, you have to set a global integer with the amount of lines there are in this block, which you have to return in the `getLinesConsumed()` method. This means that you have to count the amount of lines that belong to your block inside the `conforms()` method, which isn't desirable and should be refactored as soon as possible.

```java
public class NewRule implements Rule {

	/** Pattern used to find the start of your block */
	public static final Pattern START_PATTERN = Pattern.compile("some regex");
	
	/** Pattern used to find the end of your block */
    public static final Pattern END_PATTERN = Pattern.compile("some regex");

	/** The amount of lines of the block */
	private int mLinesConsumed;
    
	@Override
	public boolean comforms(final List<String> pMarkDownLines) {
		if (!START_PATTERN.matcher(pMarkDownLines.get(0)).matches()) {
			return false;
		}
		mLinesConsumed = 0;
		for (String line : pMarkDownLines) {
		    mLinesConsumer += 1;
		    if (END_PATTERN.matcher(line).matches()) {
		        return true;
		    }
		}
		return false;
	}

	@Override
	public int linesConsumed() {
		return mLinesConsumed;
	}

	@Override
	public MarkDownItem toMarkDownItem(final List<String> pMarkDownLines) {
	    // pMarkDownLines is now an List with all the lines in your block
		return new SomeMarkDownItem();
	}
}
```

## Inline rules

For detecting inline Markdown, like **bold** or *italic* strings, instead of extending `RegexRule` or `Rule` just extend `InlineRule` and return a `MarkDownString` instead of a `MarkDownItem`.

For example, a rule that would match %%some text%% would look like this

```java
public class PercentRule implements InlineRule {

	@Override
	public Pattern getRegex() {
		return Pattern.compile("%{2}(.+?)-{2}");
	}

	@Override
	public MarkDownString toMarkDownString(final String pContent) {
		return new PercentString(content, true);
	}
}
```

Where `PercentString` would be an extension of `MarkDownString`

```java
public class PercentString extends MarkDownString {

	public PercentString(final String pContent, final boolean pCanHasChildItems) {
		super(pContent, pCanHasChildItems);
	}
}
```

For inline Markdown, instead of extending `DisplayItem<View, Foo, Spanned>` you'd want to extend `InlineDisplayItem<Spanned, PercentString>`

```java
public class PercentInlineDisplayItem implements InlineDisplayItem<Spanned, PercentString> {

	@Override
	public Spanned create(final InlineConverter<Spanned> pInlineConverter, final PercentString pMarkDownString) {
		// return your Spannable String here
	}
}
```

And add them to `MarkyMark`s `InlineConverter<Spanned>` as explained above

```java
inlineViewConverter.addMapping(new PercentInlineDisplayItem());
```

# Download
---

Add the [Jitpack.io](www.jitpack.io) repository to your project root `build.gradle` file

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Android MarkyMark with Contentful support
```groovy
compile 'compile 'com.github.m2mobi.markymark-android:markymark-android:0.1.0' 
compile 'compile 'com.github.m2mobi.markymark-android:markymark-contentful:0.1.0' 
```

If you want to use MarkyMark outside of a Android project you might be interested in these pure Java modules

```groovy
// Base
compile 'compile 'com.github.m2mobi.markymark-android:markymark-core:0.1.0' 
// Commons
compile 'compile 'com.github.m2mobi.markymark-android:markymark-commons:0.1.0' 
```

From which you can create `MarkyMark` like so

```java
Converter<View> viewConverter = new Converter<>();
viewConverter.addMapping(new SomeItem();

InlineConverter<Spanned> inlineConverter = new InlineConverter<>();
inlineConverter.addMapping(new SomeInlineItem());

final MarkyMark<View> markyMark = new MarkyMark.Builder<View>()
				                    .addFlavor(new SomeFlavor())
				                    .setConverter(viewConverter)
				                    .setInlineConverter(inlineConverter)
			                    	.build();
```

# Contributions
---

Contributions are encouraged! Create a PR against the Development branch and always run the tests before doing so.
As of now only the `markymark-contentful` module has tests.

## Starting points for contributions

### Rule parsing

Like mentioned in **Advanced Usage**, the way rules are implemented now is:
- Pass a list of `String`s to an rule
- The rule checks whether the first string conforms to that particular `MarkDownItem`
- The rule also counts (if the first `String` conforms) how many lines belong to the item
- The parser asks the rule how many lines belong to the item
- The parser passes those lines to the rule to create the `MarkDownItem`
- The parser removes those lines from the original list of `Strings`
- Repeat

Like previously mentioned, the rule counts the amount of lines needed for its item inside the `conforms()` method, something that is 
pretty unexpected when you aren't familiar with the code base. This process needs refactoring.
  
### Table support

Implement support for tables.

# Author
---

M2mobi, info@m2mobi.com

# License
---

```
The MIT License (MIT)

Copyright (c) 2016 M2mobi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```