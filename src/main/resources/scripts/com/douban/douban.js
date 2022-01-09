// ==UserScript==
// @name         Douban Plugin
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Import a subject into database
// @author       Kingen
// @require      https://cdn.staticfile.org/jquery/3.4.1/jquery.min.js
// @match        https://movie.douban.com/subject/*/
// @match        https://movie.douban.com/subject/*/?*
// ==/UserScript==

function getApiUrl(path) {
    return 'http://localhost:8081/api' + path;
}

function parseValue(metadata, label, subject, key, handler) {
    let ele = metadata[label];
    if (ele != null && subject[key] == null) {
        let value = $.trim(ele.nextSibling.textContent);
        subject[key] = handler ? handler(value) : value;
    }
}

function parseValues(metadata, label, subject, key) {
    let ele = metadata[label];
    if (ele != null && subject[key] == null) {
        let values = $.trim(ele.nextSibling.textContent).split('/');
        for (let i = 0; i < values.length; i++) {
            values[i] = $.trim(values[i]);
        }
        subject[key] = values;
    }
}

function getVideo() {
    let html = $('script[type="application/ld+json"]').html();
    let video = JSON.parse(html.replace(/\n/g, ''));
    video.zhTitle = document.title.substr(0, document.title.length - 5);
    if (!video.name.startsWith(video.zhTitle)) {
        throw new Error('Name and zhTitle are not matched.');
    }
    if (video.name.length > video.zhTitle.length) {
        video.originalTitle = $.trim(video.name.substr(video.zhTitle.length));
    } else {
        video.originalTitle = video.zhTitle;
    }

    let metadata = {};
    $('#info span.pl').each((i, ele) => metadata[$(ele).text()] = ele);
    parseValues(metadata, '制片国家/地区:', video, 'countryOfOrigin');
    parseValues(metadata, '语言:', video, 'inLanguage');
    parseValue(metadata, 'IMDb:', video, 'imdbId');
    parseValues(metadata, '又名:', video, 'alternateName');

    if (video['@type'] === 'TVSeries') {
        parseValue(metadata, '集数:', video, 'numberOfEpisodes', value => Number(value))
    }
    return video;
}

$(function () {
    let url = $('meta[property="og:url"]').attr('content');
    let subjectId = Number(url.match(/\d+/)[0]);

    $.post({
        url: getApiUrl('/video/count'),
        contentType: 'application/json',
        data: JSON.stringify({
            doubanId: subjectId
        }),
        success: (count) => {
            if (count === 0) {
                try {
                    let video = getVideo();
                    $.post({
                        url: getApiUrl('/video/import/subject?id=' + subjectId),
                        contentType: 'application/json',
                        data: JSON.stringify(video),
                        success: () => {
                            $(".aside").prepend('<em>Imported</em>');
                        },
                        error: (xhr) => {
                            $(".aside").prepend('<p style="color: red">' + xhr.responseText + '</p>');
                        }
                    })
                } catch (error) {
                    $(".aside").prepend('<em style="color: red">' + error.message + '</em>');
                }
            } else {
                $(".aside").prepend('<em>Exists</em>');
            }
        },
        error: () => {
            $(".aside").prepend('<em style="color: red">ERROR</em>');
        }
    })
});