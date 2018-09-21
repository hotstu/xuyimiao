# xuyimiao
#### an excited android lib helping add text animation effect on any view


## How to use
1. import lib
2. attach to activity when onCreate
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DynamicView dy = DynamicView.attach2Window(this);
    }
```
3. invoke when click any view:
```
    public  void onClick(View v) {
        dy.inspect(v, "+1s");
    }
```