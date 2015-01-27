# MultiSpinner
[![Download](https://api.bintray.com/packages/thomashaertel/maven/MultiSpinner/images/download.svg) ](https://bintray.com/thomashaertel/maven/MultiSpinner/_latestVersion)
[![Build Status](https://travis-ci.org/thomashaertel/MultiSpinner.svg?branch=master)](https://travis-ci.org/thomashaertel/MultiSpinner)

Android Spinner Widget with multi selectable list

## Overview
MultiSpinner is a class with can be used by Android developers that need a spinner widget with multi selection capabilities.
When the user touches on the spinner widget a dialog pops up with a checkbox list. 

<img src="https://cloud.githubusercontent.com/assets/1078036/5889593/3e7044de-a430-11e4-91ae-2931dfa6fd22.png" alt="Spinner with single selection" width="25%" height="25%">
<img src="https://cloud.githubusercontent.com/assets/1078036/5889594/4083da9c-a430-11e4-988e-ff7dd114084b.png" alt="Spinner item selection dialog" width="25%" height="25%">
<img src="https://cloud.githubusercontent.com/assets/1078036/5889595/41dc0612-a430-11e4-83c4-fe24ff353d24.png" alt="Spinner with multiple selection" width="25%" height="25%">

## Usage
Integrating the widget is quite simple. In your `layout.xml` add the following snippet:
```xml
...
<com.thomashaertel.widget.MultiSpinner
    android:id="@+id/spinnerMulti"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
...
```

After inserting the widget in your layout add the following code to your activity:
 ```java
    public class MyActivity extends Activity {
        private MultiSpinner spinner;
        private ArrayAdapter<String> adapter;
 
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
     
            // create spinner list elements
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            adapter.add("Item1");
            adapter.add("Item2");
            adapter.add("Item3");
            adapter.add("Item4");
            adapter.add("Item5");
     
            // get spinner and set adapter
            spinner = (MultiSpinner) findViewById(R.id.spinnerMulti);
            spinner.setAdapter(adapter, false, onSelectedListener);
     
            // set initial selection
            boolean[] selectedItems = new boolean[adapter.getCount()];
            selectedItems[1] = true; // select second item
            spinner.setSelected(selectedItems);
        }

        private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
            public void onItemsSelected(boolean[] selected) {
                // Do something here with the selected items
            }
        };
    }
```

## Building
### Gradle

#### From Bintray

Add maven central to your `build.gradle`:

```groovy
buildscript {
  repositories {
    jcenter()
  }
}

repositories {
  jcenter()
}
```

#### From maven central

Add maven central to your `build.gradle`:

```groovy
buildscript {
  repositories {
    mavenCentral()
  }
}

repositories {
  mavenCentral()
}
```

Then declare MultiSpinner within your dependencies:

```groovy
dependencies {
  ...
  compile('com.thomashaertel:multispinner:0.1.0@aar') {
  }
  ...
}
```

### Maven

#### From maven central

To use MultiSpinner within your maven build simply add

```xml
<dependency>
  <artifactId>multispinner</artifactId>
  <version>${multispinner.version}</version>
  <groupId>com.thomashaertel</groupId>
</dependency>
```

to your pom.xml

If you also want the sources or javadoc add the respective classifier

```xml
  <classifier>sources</classifier>
```

or

```xml
  <classifier>javadoc</classifier>
```
to the dependency.

## License

* [MIT](http://opensource.org/licenses/MIT)