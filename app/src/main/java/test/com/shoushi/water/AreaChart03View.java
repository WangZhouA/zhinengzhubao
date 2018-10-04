/**
 * Copyright 2014  XCL-Charts
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 2.4
 */
package test.com.shoushi.water;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @ClassName AreaChart03View
 * @Description 面积图例子
 */

public class AreaChart03View extends DemoView {

    private String TAG = "AreaChart03View";
    public static final int TYPE_DAY = 1;
    public static final int TYPE_WEEK = 2;
    public static final int TYPE_MOUTH = 3;
    public static final int TYPE_YEAR = 4;


    private int type;

    private AreaChart chart = new AreaChart();
    private AreaChart chartBg = new AreaChart();


    //X轴数据
    private LinkedList<String> xLabels = new LinkedList<String>();

    //数据集合，线的集合
    private LinkedList<AreaData> mDataset = new LinkedList<AreaData>();
    DecimalFormat df = new DecimalFormat("#0");

    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();


    public AreaChart03View(Context context) {
        super(context);
        initView();
    }

    public AreaChart03View(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AreaChart03View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        //设置X轴数据
        setXData();
        //
        chartDataSet();

        //数据，背景，x轴，Y轴网格等
        chartBgRender();


        chartRender();

        //綁定手势滑动事件
        //this.bindTouch(this,chart);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chartBg.setChartRange(w, h);
        chart.setChartRange(w, h);

    }

    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);

//            chart.disableHighPrecision();
//            chart.disablePanMode();

            //轴数据源
            //标签轴
            chart.setCategories(xLabels);
            //数据轴
            chart.setDataSource(mDataset);
            //
            chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);
            //数据轴最大值
            chart.getDataAxis().setAxisMax(100);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(10);

            //网格
            chart.getPlotGrid().hideHorizontalLines();
            chart.getPlotGrid().hideVerticalLines();

            //把轴线和刻度线给隐藏起来
            chart.getDataAxis().hideAxisLine();
            chart.getDataAxis().hideTickMarks();
            chart.getCategoryAxis().hideAxisLine();
            chart.getCategoryAxis().hideTickMarks();


            //标题，子标题
            String title = "本日用水统计";
            String sonTitle = "";
//            chart.setTitle(title);
//            chart.addSubtitle("(XCL-Charts Demo)");


            //设定交叉点标签显示格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack() {
                @Override
                public String doubleFormatter(Double value) {
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(value).toString();
                    return label;
                }
            });

            //chart.getBackgroundPaint().setAlpha(254);
            //背景渐变，不是填充渐变
//            chart.getPlotArea().setBackgroundColor(true, Color.rgb(163, 69, 213));
//            chart.getPlotArea().setApplayGradient(true);
//            chart.getPlotArea().setEndColor(Color.WHITE);


            //透明度0-255
            chart.getAreaFillPaint().setAlpha(200);
            chart.setAreaAlpha(200);

            //Y轴double数据
            chart.getDataAxis().hide();
            //Y轴全部double数据
//            chart.getCategoryAxis().hide();

            //额外的线
//            CustomLineData line1 = new CustomLineData("30", mStdValue, Color.RED, 7);
//            line1.getLineLabelPaint().setColor(Color.RED);
//            line1.setLabelHorizontalPostion(Align.LEFT);
//            line1.hideLine();
//            line1.setLineStyle(XEnum.LineStyle.DASH);
//            line1.setLabelOffset(chart.getDataAxis().getTickLabelMargin());
//            mCustomLineDataset.add(line1);
//
//            CustomLineData line2 = new CustomLineData("20", 20d, Color.RED, 7);
//            line2.setLabelHorizontalPostion(Align.LEFT);
//            line2.hideLine();
//            line2.setLabelOffset(chart.getDataAxis().getTickLabelMargin());
//            line2.getLineLabelPaint().setColor(Color.RED);
//            mCustomLineDataset.add(line2);
//
//            CustomLineData line3 = new CustomLineData("10", 10d, Color.RED, 7);
//            line3.setLabelHorizontalPostion(Align.LEFT);
//            line3.hideLine();
//            line3.getLineLabelPaint().setColor(Color.RED);
//            line3.setLabelOffset(chart.getDataAxis().getTickLabelMargin());
//            mCustomLineDataset.add(line3);
//
//            chart.setCustomLines(mCustomLineDataset);
            //显示线的key名字
            chart.getPlotLegend().hide();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    //将标签与对应的数据集分别绑定
    //标签对应的数据集
    private void chartDataSet() {
        List<Double> dataSeries1 = new LinkedList<Double>();

        //设置每条线各自的显示属性
        //key,数据集,线颜色,区域颜色
        int linecolor = 0xff8392fe;
        int areaStart = 0xff8190fe;
        int areaEnd = 0xff91fdfd;
        AreaData line1 = new AreaData("小熊", dataSeries1, linecolor, Color.YELLOW);
        //不显示点
        line1.setDotStyle(XEnum.DotStyle.HIDE);
        //填充数据渐变颜色
        line1.setApplayGradient(true);
        line1.setAreaBeginColor(areaStart);
        line1.setAreaEndColor(areaEnd);
        line1.setGradientDirection(XEnum.Direction.VERTICAL);
        //设置线的大小
        line1.getLinePaint().setStrokeWidth(3);
        mDataset.add(line1);
    }


    //绘制背景区域
    private void chartBgRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chartBg.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            //？提高性能
//            chartBg.disableHighPrecision();
//            chartBg.disablePanMode();

            //轴数据源
            //标签轴,
            chartBg.setCategories(xLabels);
            //数据轴
            //chartBg.setDataSource(mDataset);
            chartBg.setCrurveLineStyle(XEnum.CrurveLineStyle.BEZIERCURVE);


//            int max = 60;
//            int min = 0;
//            //数据轴最大值,Y轴
//            chartBg.getDataAxis().setAxisMax(max);
//            chartBg.getDataAxis().setAxisMin(min);
//            //数据轴刻度间隔
//            chartBg.getDataAxis().setAxisSteps(10);
//            //Y轴字体颜色
//            chartBg.getDataAxis().getTickLabelPaint().setColor(Color.GRAY);

            //网格显示
            float vStrokeWidth = 0.2f;
            float hStrokeWidth = 0.2f;
//            chartBg.getPlotGrid().showHorizontalLines();
            chartBg.getPlotGrid().showVerticalLines();
            //连续线
            chartBg.getPlotGrid().setHorizontalLineStyle(XEnum.LineStyle.SOLID);
            chartBg.getPlotGrid().setVerticalLineStyle(XEnum.LineStyle.SOLID);
            chartBg.getPlotGrid().getVerticalLinePaint().setColor(0xffededed);
            chartBg.getPlotGrid().getHorizontalLinePaint().setColor(0xffededed);
            chartBg.getPlotGrid().getVerticalLinePaint().setStrokeWidth(vStrokeWidth);
            chartBg.getPlotGrid().getHorizontalLinePaint().setStrokeWidth(hStrokeWidth);

            //把轴线和刻度线给隐藏起来

            //Y轴
            int linecolor = 0xff8392fe;
            chartBg.getDataAxis().getAxisPaint().setColor(linecolor);
            chartBg.getDataAxis().getAxisPaint().setStrokeWidth(3);
            //chartBg.getDataAxis().hideAxisLine();
            chartBg.getDataAxis().hideTickMarks();

            //X轴
            int xlinecolor = 0xff86d3fa;
            chartBg.getCategoryAxis().getAxisPaint().setColor(xlinecolor);
            chartBg.getCategoryAxis().getAxisPaint().setStrokeWidth(3);
            //chartBg.getCategoryAxis().hideAxisLine();
            chartBg.getCategoryAxis().hideTickMarks();


            //定义数据Y轴标签显示格式,
            //这里小于30不显示
            chartBg.getDataAxis().setLabelFormatter(new IFormatterTextCallBack() {
                @Override
                public String textFormatter(String value) {
                    Double tmp = Double.parseDouble(value);
                    if (tmp == 0) {
                        return "";
                    }
                    String label = df.format(tmp).toString();
                    return (label);
                }
            });
            //？
//            chartBg.getPlotLegend().hide();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }


    //设置X轴数据
    private void setXData() {
        for (int i = 0; i < 7; i++) {
            xLabels.add(i + "");
        }
    }

    //刷新数据
    public void refreshChart(int type, List<String> X, List<Double> data, int max, int min, int step) {
        this.type = type;
        //X轴,每一个传进去的X数据对应一个数据格式
        //所以
        xLabels.clear();
        xLabels.addAll(X);
        setXLabelFormatter(type);

        //数据更新
        chart.getDataAxis().setAxisMax(max);
        chart.getDataAxis().setAxisMin(min);
        mDataset.get(0).getLinePoint().clear();
        mDataset.get(0).getLinePoint().addAll(data);
        chart.setDataSource(mDataset);

        //Y轴
        chartBg.getDataAxis().setAxisMax(max);
        chartBg.getDataAxis().setAxisMin(min);
        chartBg.getDataAxis().setAxisSteps(step);

        this.invalidate();
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chartBg.render(canvas);
            chart.render(canvas);

            //画顶上的标题
//            Paint paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setColor(Color.rgb(11, 35, 122));
//            paint.setTextSize(22);
//            canvas.drawText("2015/10/26 晚上12点  周日  价位:xxxx",
//                    chart.getPlotArea().getLeft(), chart.getPlotArea().getTop() - 10.f, paint);

            if (type == TYPE_DAY) {
//                drawXtitle(canvas, "单位/小时");
                drawYtitle(canvas, "步");
            } else if (type == TYPE_WEEK) {
//                drawXtitle(canvas, "单位/周");
                drawYtitle(canvas, "步");
            } else if (type == TYPE_MOUTH) {
//                drawXtitle(canvas, "单位/日");

                drawYtitle(canvas, "步");
            } else if (type == TYPE_YEAR) {
//                drawXtitle(canvas, "单位/月");
                drawYtitle(canvas, "步");
            }


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    //画X轴标题
    void drawXtitle(Canvas canvas, String txt) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setTextSize(22);
        float txtleght = paint.measureText(txt);
        canvas.drawText(txt, chart.getRight() / 2 - txtleght / 2, chart.getBottom() - 5, paint);


    }

    //画Y轴标题
    void drawYtitle(Canvas canvas, String txt) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setTextSize(22);
        canvas.drawText(txt, chart.getPlotArea().getLeft(), chart.getPlotArea().getTop() - 10, paint);
    }


    void setXLabelFormatter(final int type) {
        //X轴数据格式
        chartBg.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack() {
            @Override
            public String textFormatter(String value) {
                int i = Integer.parseInt(value);
                if (type == TYPE_MOUTH) {
                    if (i % 2 == 0) {
                        return "";
                    }
                }

                if (type == TYPE_DAY) {
                    if ((i + 1) % 2 == 0) {
                        return "";
                    }
                }
                String label = df.format(i).toString();
                return label;
            }
        });
        //X轴数据格式
        chart.getCategoryAxis().setLabelFormatter(new IFormatterTextCallBack() {
            @Override
            public String textFormatter(String value) {
                int i = Integer.parseInt(value);
                if (type == TYPE_MOUTH) {
                    if (i % 2 == 0) {
                        return "";
                    }
                }
                if (type == TYPE_DAY) {
                    if ((i + 1) % 2 == 0) {
                        return "";
                    }
                }
                String label = df.format(i).toString();
                return label;
            }
        });

    }

}

