#Android Data Binding 使用

谷歌在很早的时候就推出了Android Data Binding 数据绑定的方案，我们团队差不多是第一批把Data Binding 放入真正的项目中去使用的：

>本文针对有MVVM 基础的朋友去阅读，主要分享在实际开发中出现的一些问题已经使用介绍。如果不清楚的可以点击链接去了解基础内容（需要爬墙）
>https://developer.android.com/topic/libraries/data-binding/index.html 

- **bindEditText**
- **bindListViewApapter**
- **bindRecycleApapter**
- **bindImageView**
- **简单demo**

-------------------


## bindEditText

>EditText数据绑定，页面用户输入数据，我们对应的Model需要发生改变，这里我们首先需要先得到EditText 然后通过 addTextChangedListener 监听数据内容改变并更新Model，这里Model就是我们的第二个参数我们使用自己封装的一个BindableString


代码示例

BindingAdapter

``` java
@BindingAdapter({"app:binding"})
    public static void bindEditText(EditText view,
                                    final BindableString bindableString) {
        if (view.getTag(R.id.bindEditTextTag) != bindableString) {
            view.setTag(R.id.bindEditTextTag, bindableString);
            view.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bindableString.set(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        String newValue = bindableString.get();
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }

``` 
BindableString  

```java
//这里我们需要继承Observable
public class BindableString extends BaseObservable{
    private String value = "";

    public String get() {
        return value;
    }

    public void set(String _value) {

        this.value = _value;
        notifyChange();
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }
}
```
布局文件使用

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="viewModel"         type="com.baisoo.myapplication_databinding.ViewModel"/>
        <import type="android.view.View"/>
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        >
        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="田老师好"
            app:editText="@{viewModel.ext}"
            />
    </LinearLayout>
</layout>
```
ViwModel

```java
public class ViewModel extends BaseObservable {
    public BindingString ext = new BindingString();
}
```

### bindListViewApapter
>ListView可以说是我们项目里面非常常用的一个控件，但是我们却一直在做重复的事情，就是我们要根据不同的数据和视图去写不同的适配器，但是我们使用BindingAdapter之后大部分视图展示我们只需要关系数据（model）即可，视图的数据我们则不需要去关系，下面看具体实现方法。

ListViewBindingAdapter

```java
@android.databinding.BindingAdapter({"app:item", "app:data"})
    public static void ListViewBindingAdapter(final ListView view, final int item, final List data) {

        if (view.getTag() == null) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(view.getContext(), item, data);
            view.setAdapter(listViewAdapter);
            view.setTag(listViewAdapter);
        } else {

            view.post(new Runnable() {
                @Override
                public void run() {
                    ((ListViewAdapter) (view.getTag())).notifyDataSetChanged();
                    view.setSelection(data.size());
                }
            });
        }
    }
```

实现了数据绑定的ListViewAdapter 

```java
public class ListViewAdapter extends BaseAdapter {
    private Context contextl;
    private List data;
    private int item;
    public ListViewAdapter(Context contextl,int item,List data){
        this.contextl=contextl;
        this.data=data;
        this.item=item;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding viewDataBinding;
        if (convertView == null) {
            viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(contextl),
                    item, parent, false);
            convertView = viewDataBinding.getRoot();
            convertView.setTag(viewDataBinding);
        } else {
            viewDataBinding = (ViewDataBinding) convertView.getTag();
        }
       //把当前的数据传递给Item viewDataBinding.setVariable(com.baisoo.myapplication_databinding.BR.item,
                data.get(position));
      //把当前的position 传递给Item   viewDataBinding.setVariable(com.baisoo.myapplication_databinding.BR.index,
                position);

        return viewDataBinding.getRoot();
    }
}
```
主布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="viewModel"
    type="com.baisoo.myapplication_databinding.ViewModel"/>
        <import type="android.view.View"/>
    </data>
    
        <ListView
            android:id="@+id/listView"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:data="@{viewModel.mData}"
            app:item="@{@layout/item}"
            />
    </LinearLayout>
</layout>
```
ListView Item 布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        >

    <data>
       <!-- 数据文件 根据自己 使用 去定义 这里使用String 类型-->
        <variable
            name="item"
            type="String"/>
        <variable
            name="index"
            type="int"/>
    </data>

    <LinearLayout
                  android:layout_width="match_parent"
                  android:layout_height="match_parent"
                  android:orientation="vertical">
      <!--这里设置TextView的Tag是为了可以让外界通过view.getTag() 得到当前的index-->
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:onClick="test"
                android:text="@{item+index}"
                android:tag="@{index}"/>
    </LinearLayout>
</layout>
```
ViewModel 

``` java
public class ViewModel extends BaseObservable {
    public ObservableList<String> mData = new ObservableArrayList<>();
   
    private Context context;
    public ViewModel(Context context) {
        this.context = context;
    }
    public void add() {
        for (int i = 0; i < 50; i++) {
            mData.add(i + "wo shi shu ju ");
        }
    }
    public void addto() {
        mData.add(" wo shi  hou  lia  de");
        mData.add(" wo shi  hou  lia  de2");
    }
}
```

MainActivity

```java
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_main);
        viewModel= new ViewModel(this);
        activityMainBinding.setViewModel(viewModel);
        activityMainBinding.listView.addHeaderView(new TextView(this));
        viewModel.add();
    }
    public void test(View v ){
        Toast.makeText(v.getContext(),v.getTag()+"",Toast.LENGTH_LONG).show();
        viewModel.addto();
    }
}
```
### bindRecycleApapter

>RecycleView实现起来和ListView一样甚至更简单 所以这里就不占篇幅了

###bindImageView

>我们项目里会有很多图片显示这些图片大多数都是使用主流的第三方控件，但是也有一个问题就是，我们在代码里写了太多重复的没有用的代码，如果我们使用BindingAdapter 就不会这样，我们更方便进行管理这些第三方控件，这里我只是使用ImageView做一个例子

布局文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools">
    <data>
        <variable
            name="viewModel"        type="com.baisoo.myapplication_databinding.ViewModel"/>
        <import type="android.view.View"/>
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        >

        <ImageView
            android:layout_width="200dp"
            android:layout_height="200dp"
            android:onClick="url"
            app:imageUrl="@{viewModel.url}"
            />
  </LinearLayout>
</layout>
```
bindingAdapter

```java
  @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }
```

是不是很爽，这样我们所有的第三方全部可以走到一个方法里面去，这样一来非常方便管理。

*这是我第一次写技术类博客希望得到大家的支持，有不足的地方大家可以提出想 *

QQ：3126223094 加好友请注明

***Demo 地址 欢迎下载 ***
***https://github.com/waddwaw/MyApplication_Databinding***
