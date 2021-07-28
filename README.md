# Play a YouTube video in the background


Righto. I should be a millionaire by now; I solved society's biggest issue. Too bad I can't publish it, because Google doesn't approve of this functionality

My best guess is because playing a video in the background is simply bad practise. The phone would unnecessarily use its power to play a video you're not looking at. However,
some people would want this exact feature, so they can listen to music with their phone closed

Anyway, enough small talk. Let's get to business

# Android Lifecycles

The reason I made this app was to experiment with Android lifecycles and Timber. 

## Use Timber

Timber is a library that makes logging a lot easier and nicer. You don't have to use the good ol' dreadful `Log.i("MainActivity", "ello ello")`

### 1. Make the App.kt file

You heard me. I said kt file. Not java file. Oh you still use java? Haha ok grandad. 

You need to extend from Application, override onCreate. Like so

```
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
```

### 2. Edit AndroidManifest

This step is easy to forget. Go ahead and make this tiny tweak

```
    <application
        android:name=".App"
```

### 3. Log a message

Go ahead and within onCreate or somewhere else, log a message
```
Timber.i("ello ello")
```

## Use lifecycles

The lifecycle is as following: onCreate() > onStart() > onResume() > onPause() > onStop() > onDestroy()

Memorise this like your life depends on it. Anyway lets explain the best way to use lifecycles

The old fashioned way is by overriding onStart() or the other methods directly within your activity. The better way is by using an Observer class

### 1. Create an Observer class

In the example below, you must pass into the constructor parameter a lifecycle instance. The Observer must also extend from LifecycleObserver

Use annotations to override lifecycle functions. 

```
class YouTubeObserver(lifecycle: Lifecycle, var youTubePlayerView: YouTubePlayerView) : LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Timber.i("onDestroy called, releasing video")
        youTubePlayerView.release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Timber.i("onCreate called")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Timber.i("onPause called")
    }


}
```

### 2. Use your Observer within MainActivity

Firstly, your MainActivity must extend LifecycleObserver

Go ahead and lateinit your observer like so: `private lateinit var observer: YouTubeObserver`

If you're using Java, then use: `ha ha you're still using Java old man? ha ha fooled you`

Now create your observer object within onCreate() like so `observer = YouTubeObserver(this.lifecycle, youTubePlayerView)`

Remember to init it within onCreate() because you made a promise to Kotlin that you will init it, because you used lateinit. If you break your promise, Kotlin will be upset
and throw an exception and cry because you didn't init a lateinit var, and because you broke Kotlin's heart

Keep in mind, the youTubePlayerView is an optional parameter I added in. You don't need to add it. The `this.lifecycle` is required though. 

Because I wanted to pass in youTubePlayerView onto the observer, to allow the observer to use it. In this case, it is to call `youTubePlayerView.release()` onto it
