package com.yq.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * http://www.jianshu.com/p/5c221c58e141
 */
public class SecondActivity extends Activity {

    private static final String TAG = SecondActivity.class.getSimpleName();

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tv = (TextView) findViewById(R.id.tvSecondActivity);

//        scheduler();
        map();
    }

    /**
     * Scheduler
     */
    private void scheduler() {
        Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                Data data = getData();
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Data>() {
            @Override
            public void onCompleted() {
                Log("Schedulers onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log("Schedulers onError");
            }

            @Override
            public void onNext(Data data) {
                Log("onNext on thread: " + Thread.currentThread().getName() + ", data: " + data.toString());
                tv.setText(data.toString());
            }
        });
    }
    //模拟数据库读数据
    private Data getData() {
        Log("geting data on thread: " + Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Data("this is a data", 3132342);
    }

    //常用操作符-------------------------------------start
    /**
     *map 最常用且最实用的操作符之一，将对象转换成另一个对象发射出去，应用范围非常广，如数据的转换，数据的预处理等
     */
    private void map() {
        Observable.just("123456", "abcdefg").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s.substring(0, 3);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log("map: " + s);
            }
        });
    }

    /**
     * 和Map很像但又有所区别，Map只是转换发射的数据类型，而FlatMap可以将原始Observable转换成另一个Observable。
     * 还是举例说明吧。假设要打印全国所有学校的名称，可以直接用Map
     */
    private void flatMap() {
        List<School> schoolList = new ArrayList<>();
        Observable.from(schoolList).flatMap(new Func1<School, Observable<Student>>() {
            @Override
            public Observable<Student> call(School school) {
                return Observable.from(school.getStudents());
            }
        }).subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                Log("flatMap: " + student.getName());
            }
        });
    }

    /**
     * 缓存，可以设置缓存大小，缓存满后，以list的方式将数据发送出去
     */
    private void buffer() {
        Observable.just(1, 2, 3).buffer(2).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                Log("buffer: " + integers.size());
            }
        });//output: buffer: 2          bffer: 1

        /*
        在开发当中，个人经常将Buffer和Map一起使用，常发生在从后台取完数据，
        对一个List中的数据进行预处理后，再用Buffer缓存后一起发送，保证最后数据接收还是一个List
         */
        List<School> schoolList = new ArrayList<>();
        Observable.from(schoolList).map(new Func1<School, School>() {
            @Override
            public School call(School school) {
                school.setName("NB大学");  //将所有学校改名
                return school;
            }})
                .buffer(schoolList.size())  //缓存起来，最后一起发送
                .subscribe(new Action1<List<School>>() {
                    @Override
                    public void call(List<School> schools) {
                    }});
    }

    /**
     * take 发射前n项数据，还是用上面的例子，假设不要改所有学校的名称了，就改前四个学校的名称
     */
    private void take() {
        List<School> schoolList = new ArrayList<>();
        Observable.from(schoolList).take(4).map(new Func1<School, School>() {
            @Override
            public School call(School school) {
                school.setName("NB大学");
                return school;
            }}).buffer(4).subscribe(new Action1<List<School>>() {
            @Override
            public void call(List<School> schools) {
            }});
    }

    /**
     * distinct 去掉重复的项
     */
    private void distinct() {
        Observable.just(1, 2, 1, 1, 2, 3)
                .distinct()
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer item) {
                        Log("distinct: " + item);
                    }
                });
    }

    /**
     * filter 过滤，通过谓词判断的项才会被发射，例如，发射小于4的数据
     */
    private void filter() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return (integer < 4);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log("filter: " + integer);
                    }
                });
    }

    //常用操作符-------------------------------------end

    private void Log(String content) {
        Log.d(TAG, content);
    }

    private static class Data {
        public Data(String content, long time) {
            this.content = content;
            this.time = time;
        }

        String content;
        long time;

        @Override
        public String toString() {
            return content + ", " + time;
        }
    }

    private static class School {
        private String name;
        private List<Student> students;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Student> getStudents() {
            return students;
        }

        public void setStudents(List<Student> students) {
            this.students = students;
        }
    }
    private static class Student {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
