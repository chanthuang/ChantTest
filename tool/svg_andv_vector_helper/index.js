const fs = require('fs');

const CHANT_TEST_PROJ = '/Users/chant/android/ChantTest/';
const RES_DIR = CHANT_TEST_PROJ + 'app/src/main/res/';
const SVG_DIR = RES_DIR + 'raw/';
const DRAWABLE_DIR = RES_DIR + 'drawable/';

const WR_PROJ = '/Users/chant/android/WeRead/';
const SRC_DRAWABLE_DIR = WR_PROJ + 'res/drawable/';

// 遍历SVG文件，把对应的VectorDrawable文件拷贝到 drawable 目录。
// 如果不存在 VectorDrawable 文件，则删除 SVG 文件。
// 最终保证 SVG 文件和 VectorDrawable 文件一一对应。
function copyVectorDrawableFiles() {
    var allSVGFiles = fs.readdirSync(SVG_DIR);
    allSVGFiles.forEach(function (svgFile, index, array) {
        var srcFile = SRC_DRAWABLE_DIR + svgFile.replace('.svg', '.xml');
        var dstFile = DRAWABLE_DIR + "vd_" + svgFile.replace('.svg', '.xml');
        var exist = fs.existsSync(srcFile);
        if (exist) {
            fs.stat(srcFile, function (err, st) {
                if (err) throw err;
                if (st.isFile()) {
                    var readable = fs.createReadStream(srcFile);
                    var writable = fs.createWriteStream(dstFile);
                    readable.pipe(writable);
                }
            });
        } else {
            console.log('VectorDrawable 文件不存在：' + srcFile + ", 删除 SVG 文件：" + svgFile);
            fs.unlink(SVG_DIR + svgFile);
        }
    });
}

/**
 * @returns {string} layout 文件内容的外框架
 */
function layoutContent() {
    return ['<?xml version="1.0" encoding="utf-8"?>',
        '<ScrollView',
        'xmlns:android="http://schemas.android.com/apk/res/android"',
        'xmlns:app="http://schemas.android.com/apk/res-auto"',
        'android:layout_width="match_parent"',
        'android:layout_height="match_parent" >',
        '<com.chant.chanttest.svg.TraceLinearLayout',
        'android:id="@+id/activity_main"',
        'android:layout_width="match_parent"',
        'android:layout_height="wrap_content"',
        'android:gravity="center"',
        'android:orientation="vertical">',
        '$CONTENT$',
        "</com.chant.chanttest.svg.TraceLinearLayout>",
        "</ScrollView>"
    ].join('\n');
}

/**
 * 生成 svg layout 文件
 */
function makeSVGLayoutFile() {
    var dstLayoutFile = RES_DIR + 'layout/activity_svg_icon.xml'
    fs.writeFile(dstLayoutFile, "");
    var str = layoutContent();
    var imageStrArray = [];
    var allSVGFiles = fs.readdirSync(SVG_DIR);
    allSVGFiles.forEach(function (svgFile, index, array) {
        if (svgFile.endsWith('.svg')) {
            imageStrArray.push(
                '<com.chant.chanttest.svg.SVGImageView android:layout_width="wrap_content" android:layout_height="wrap_content" android:src="@raw/$resName$"/>'
                    .replace('$resName$', svgFile.slice(0, -4))
            );
        }
    });
    fs.appendFile(dstLayoutFile, str.replace('$CONTENT$', imageStrArray.join('\n')));
}

/**
 * 生成 vectorDrawable layout 文件
 */
function makeVDLayoutFile() {
    var dstLayoutFile = RES_DIR + 'layout/activity_vector_icon.xml'
    fs.writeFile(dstLayoutFile, "");
    var str = layoutContent();
    var imageStrArray = [];
    var allVDFiles = fs.readdirSync(DRAWABLE_DIR);
    allVDFiles.forEach(function (vdFile, index, array) {
        if (vdFile.endsWith('.xml')) {
            imageStrArray.push(
                '<android.support.v7.widget.AppCompatImageView android:layout_width="wrap_content" android:layout_height="wrap_content" app:srcCompat="@drawable/$resName$"/>'
                    .replace('$resName$', vdFile.slice(0, -4))
            );
        }
    });
    fs.appendFile(dstLayoutFile, str.replace('$CONTENT$', imageStrArray.join('\n')));
}

// copyVectorDrawableFiles();
// makeSVGLayoutFile();
// makeVDLayoutFile();

