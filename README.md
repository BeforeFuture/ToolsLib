# ToolsLib
Android 开发工具包

旨在版主Android开发者节省开发时间，提升开发效率。



使用方法：

第一步项目的build.gradle里，加入如下代码：

allprojects {

		repositories {

			...
			maven { url 'https://jitpack.io' }

		}

	}




第二步，在moudle的build.gradle里，加入如下代码：

implementation 'com.github.BeforeFuture:ToolsLib:1.0.2'