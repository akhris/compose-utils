# compose-utils
It's implied to have some set of Android Compose utils here. For now there is just one:

## Swipe to reveal
Small lib inspired by built-in SwipeToDismiss Composable providing adjustable swipe-to-reveal behavior in Android Compose.

### Different background buttons behavior:
#### 1. Stretching behavior.
![Stretching behavior](/SwipeToReveal/gifs/1.stretching_buttons.gif)

#### 2. Overlapping behavior.
![Overlapping behavior](/SwipeToReveal/gifs/2.overlapping_buttons.gif)

#### 3. Fixed position behavior.
![Fixed position behavior](/SwipeToReveal/gifs/3.fixed_buttons.gif)

#### 4. Auto toggling event when swiping further. Or not.
![No auto fire](/SwipeToReveal/gifs/4.overlap_no_autofiring.gif)


## Installation
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency:
```
   dependencies {
	        implementation 'com.github.akhris.compose-utils:swipetoreveal:0.1.0'
	 }
```

## Usage
See example of usage in the [demo app](/app/src/main/java/com/akhris/composeutils/MainActivity.kt#L106)

## License
    Copyright 2022 Anatoly Khristianovsky

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
