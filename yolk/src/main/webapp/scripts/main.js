'use strict';

// login & signup

$('#signupBtn').click(function () {
    var data = $('form').serialize();
    var signup = $.ajax({
        type: 'POST',
        data: data,
        dataType: 'JSON',
        url: 'user/register.json',
        success: function success(resultsData, status) {
            var results = JSON.parse(resultsData);
            if (results.success === 'true') {
                console.log('signup success');
                window.location.href = 'timeline.html';
            } else if (results.success === 'false') {
                console.log('signup failure');
                alert(results.errorMsg);
            }
        }
    });
});

$('#loginBtn').click(function () {
    var data = $('form').serialize();
    var login = $.ajax({
        type: 'POST',
        data: data,
        dataType: 'JSON',
        url: 'user/login.json',
        success: function success(resultsData, status) {
            var results = JSON.parse(resultsData);
            if (results.success === 'true') {
                console.log(results);
                window.location.href = 'timeline.html';
                rememberPassword();
            } else if (results.success === 'false') {
                console.log('login failed');
                // show login error message
                $('#alertDiv').removeClass('hidden');
            }
        }
    });
});

function rememberPassword() {
    if ($('#remember').is(':checked')) {
        var username = $('#username').val();
        var password = $('#password').val();
        // set cookies to expire in 14 days
        Cookies.set('username', username, { expires: 14 });
        Cookies.set('password', password, { expires: 14 });
        Cookies.set('remember', true, { expires: 14 });
    } else {
        // reset cookies
        Cookies.set('username', null);
        Cookies.set('password', null);
        Cookies.set('remember', null);
    }
}

//logout function
$('#logoutBtn').click(function () {
    var logout = $.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: 'user/logout.json',
        success: function success(resultsData, status) {
            var results = JSON.parse(resultsData);
            if (results.success === 'true') {
                console.log('logout success');
                window.location.href = 'index.html';
            } else if (results.success === 'false') {
                console.log('logout failure');
                alert(results.errorMsg);
            }
        }
    });
});

$(document).ready(function () {
    var remember = Cookies.get('remember');
    if (remember === 'true') {
        var username = Cookies.get('username');
        var password = Cookies.get('password');
        // autofill the fields
        $('#username').val(username);
        $('#password').val(password);
        $('#remember').prop('checked', true);
    }
});

// share
$('#shareBtn').click(function () {
    var selectedItemID = getSelectedItemID();
    var username = $('#logoutBtn').data('username');
    $.ajax({
        url: 'content/share.json',
        type: 'POST',
        dataType: 'JSON',
        data: { id: selectedItemID }
    }).done(function (resultsData, textStatus, jqXHR) {
        console.log(resultsData);
        var results = $.parseJSON(resultsData);
        if (results.success === 'true') {
            var host = $(location).attr('hostname');
            var protocol = $(location).attr('protocol');
            var port = $(location).attr('port');
            var path = '/yolk/share.html';
            $('#shareURLForm').val(protocol + '//' + host + ':' + port + path + '?username=' + username + '&id=' + results.id);
            $('#shareModal').modal('toggle');
        } else if (results.success === 'false') {
            console.log('share failed');
            alert(results.errorMsg);
        }
    });
});

function getSelectedItemID() {
    return $('[class="tab-pane active"]').attr('id');
}

function loadShareContent() {
    var ids = $.urlParam('id');
    $.ajax({
        url: 'content/query.json',
        type: 'POST',
        dataType: 'JSON',
        data: { id: ids }
    }).done(function (resultsData, textStatus, jqXHR) {
        console.log(resultsData);
        var results = $.parseJSON(resultsData);
        if (results.success === 'true') {
            createShareContentDOM(results.shareContent);
        } else if (results.success === 'false') {
            console.log('load content failed');
            alert(results.errorMsg);
        }
    });
}

function createShareContentDOM(shareContent) {
    // [username] share to you
    console.log(shareContent);
    var username = shareContent.sharedByUsername;
    console.log('username:', username);
    var a = $('<a></a>', {
        href: '#',
        text: username
    });
    var p = $('<p></p>', {
        'class': 'lead share-user',
        text: ' share to you'
    }).prepend(a);
    var hr = $('<hr>');

    $('#shareContainer').append(p, hr);

    // img + text
    var contents = shareContent.contents;
    console.log('shareContent.contents: ', contents);
    for (var i = contents.length - 1; i >= 0; i--) {
        var content = contents[i];
        var text = $('<p></p>', {
            'class': 'lead',
            text: content.text
        });
        if (content.picName != undefined) {
            console.log('has picture');
            var src = 'pic/download.json?username=' + username + '&fileName=' + content.picName;
            var img = $('<img>', {
                src: src
            });
            $('#shareContainer').append(img, text);
        } else {
            $('#shareContainer').append(text);
        }
        if (i != 0) {
            $('#shareContainer').append(hr);
        }
    }

    // footer
    var footerText = $('<p></p>', {
        'class': 'pull-right',
        text: '❤️  from the Yolk team'
    });
    var footer = $('<div></div>', {
        'class': 'footer'
    }).append(footerText);
    $('#shareContainer').append(footer);
}

//get init data function
function getAllContent() {
    var data = 'start=0&pagesize=10';

    $.ajax({
        type: 'POST',
        data: data,
        dataType: 'JSON',
        url: 'content/batchquery.json',
        success: function success(resultsData, status) {
            // stop loading
            $('#loader').addClass('hidden');

            var results = JSON.parse(resultsData);

            if (results.success === 'true') {
                (function () {
                    var myContents = results.myContents;
                    console.log('myContents', myContents);

                    var username = myContents[0].sharedByUsername;
                    // store username in logout button for later use
                    $('#logoutBtn').data('username', username);

                    // structure
                    var tabPanel = $('<div/>', {
                        'role': 'tabpanel'
                    }).appendTo('#mainContainer');

                    var navTabs = $('<ul/>', {
                        'role': 'tablist',
                        'class': 'nav nav-tabs'
                    });
                    var tabContent = $('<div/>', {
                        'class': 'tab-content'
                    });
                    tabPanel.append(navTabs, tabContent);

                    // myContents.count

                    var _loop = function _loop(i) {
                        // nav tabs
                        var apanel = $('<a/>', {
                            'href': '#panel-' + (i + 1),
                            'role': 'tab',
                            'data-toggle': 'tab',
                            'id': '#panel-' + (i + 1),
                            'aria-controls': 'panel-' + (i + 1),
                            'text': 'Panel ' + (i + 1)
                        });
                        var panel = $('<li/>', {
                            'role': 'presentation'
                        }).append(apanel);
                        navTabs.append(panel);

                        // tab panes
                        var tabPane = $('<div/>', {
                            'role': 'tabpanel',
                            'class': 'tab-pane',
                            'id': myContents[i].id
                        }).appendTo(tabContent);

                        // default set panel 1 active
                        if (i == 0) {
                            panel.addClass('active');
                            tabPane.addClass('active');
                        }

                        var masonryContainer = $('<div/>', {
                            'class': 'row masonry-container'
                        }).appendTo(tabPane);

                        // relayout when switching panel
                        panel.on('shown.bs.tab', function (event) {
                            event.preventDefault();
                            layout(masonryContainer);
                        });

                        var contents = myContents[i].contents;

                        var _loop2 = function _loop2(j) {
                            var item = $('<div/>', {
                                'class': 'col-md-4 col-sm-6 item'
                            }).appendTo(masonryContainer);

                            var thumbnail = $('<div/>', {
                                'class': 'thumbnail'
                            }).appendTo(item);

                            // construct content
                            var content = contents[j];
                            // img div
                            if (content.picName) {
                                var img = $('<img>', {
                                    'src': 'pic/download.json?username=' + username + '&fileName=' + content.picName
                                }).appendTo(thumbnail);
                            }

                            // caption div
                            var caption = $('<div/>', {
                                'class': 'caption'
                            }).appendTo(thumbnail);

                            // $('<h3>', {
                            //     text: 'Thumbnail label',
                            // }).appendTo(caption);
                            if (content.text) {
                                $('<p>', {
                                    text: content.text
                                }).appendTo(caption);
                            }
                            var delBtn = $('<a>', {
                                'href': '#',
                                'class': 'btn btn-danger',
                                'role': 'button',
                                text: 'Delete'
                            });
                            delBtn.on('click', function (event) {
                                event.preventDefault();
                                $.ajax({
                                    url: 'content/delete.json',
                                    type: 'POST',
                                    dataType: 'JSON',
                                    data: { id: myContents[i].id }
                                }).done(function (resultsData, textStatus, jqXHR) {
                                    console.log('delete: ', resultsData);
                                    var results = $.parseJSON(resultsData);
                                    if (results.success === 'true') {
                                        delBtn.parents('.item').remove();
                                        layout(masonryContainer);
                                    } else {
                                        alert(results.errorMsg);
                                    }
                                });
                            });
                            $('<p>').append(delBtn).appendTo(caption);
                        };

                        for (var j = 0; j < contents.length; ++j) {
                            _loop2(j);
                        } // end for contents

                        // init layout after all elements created
                        layout(masonryContainer);
                    };

                    for (var i = 0; i < myContents.length; ++i) {
                        _loop(i);
                    } // end for myContents

                    // footer
                    var footerText = $('<p></p>', {
                        'class': 'pull-right',
                        text: '❤️  from the Yolk team'
                    });
                    var footer = $('<div></div>', {
                        'class': 'footer'
                    }).append(footerText);
                    $('#mainContainer').append(footer);
                })();
            } else if (results.success === 'false') {
                console.log('batchquery failure');
                window.location.href = '404.html';
            }
        }
    });
}

//delete item function
function deleteItem(button) {}

//for textContent
var textContent = {};
//for image name
var picContent = {};
//for count
var count = 0;

//upload only one file each time
function uploadOneFile(a) {
    //    if($('textarea').val() == '' && $('#uploadFileInput')[0].value == '')
    //        return ;
    //    if($('textarea').val() != '' && $('#uploadFileInput')[0].value != ''){
    //        $.ajax({
    //            url: 'pic/upload.json',
    //            fileElementId: 'uploadFileInput',
    //            type: 'POST',
    //            cache: false,
    //            processData: false,
    //            contentType: 'multipart/form-data;boundary='+Math.floor(Math.random()*10000).toString(),
    //            data: new FormData($('#uploadFileInput')[0]),
    //            success: function(resultsData, status) {
    //                let results = JSON.parse(resultsData);
    //                if (results.success === 'true') {
    //                    texContent.append(count.toString()+':'+$('textarea').val());
    //                    picContent.append(count.toString()+':'+results.picName);
    //                    count++;
    //                    console.log('upload success');
    //                    //close modal
    //                    //$('#uploadModal').modal('hide');
    //                } else if (results.success === 'false') {
    //                    console.log('logout failure');
    //                    alert(results.errorMsg);
    //                }
    //            },
    //            abort:function(resultsData,status){
    //                console.log(resultsData);
    //            }
    //        });
    //    }
    //    if($('textarea').val() != '' && $('#uploadFileInput')[0].value == ''){
    //        texContent.append(count.toString()+':'+$('textarea').val());
    //        count++;
    //    }
    //    if($('textarea').val() == '' && $('#uploadFileInput')[0].value != ''){
    //        $.ajax({
    //            url: 'pic/upload.json',
    //            fileElementId: 'uploadFileInput',
    //            type: 'POST',
    //            cache: false,
    //            processData: false,
    //            contentType: 'multipart/form-data;boundary='+Math.floor(Math.random()*10000).toString(),
    //            data: new FormData($('#uploadFileInput')[0]),
    //            success: function(resultsData, status) {
    //                let results = JSON.parse(resultsData);
    //                if (results.success === 'true') {
    //                    picContent.append(count.toString()+':'+results.picName);
    //                    count++;
    //                    console.log('upload success');
    //                    //close modal
    //                    //$('#uploadModal').modal('hide');
    //                } else if (results.success === 'false') {
    //                    console.log('logout failure');
    //                    alert(results.errorMsg);
    //                }
    //            },
    //            abort:function(resultsData,status){
    //                console.log(resultsData);
    //            }
    //        });
    //    }

    $.ajaxFileUpload({
        url: 'pic/upload.json',
        secureuri: false,
        fileElementId: 'uploadFileInput',
        dataType: 'json',
        success: function success(data, status) {
            console.log(data);
            alert(data);
            var result = JSON.parse(data);
            if (result.success === true) {
                alert(results.picName);
            } else {
                alert(data);
            }
        },
        error: function error(data, status, e) {
            alert(e);
        },
        abort: function abort(data, status, e) {
            console.log(data);
        }
    });
}

//haven't let the pic show on page!
//upload function
$('#uploadBtn').click(function () {
    var formdata = new FormData();
    formdata.append('image', $('#uploadFileInput')[0].files[0]);
    formdata.append('imageLabel', $('#imageLabel').value);
    formdata.append('description', $('#description').value);

    $.ajax({
        url: 'content/publish.json',
        fileElementId: '1',
        type: 'POST',
        cache: false,
        data: formdata,
        processData: false,
        contentType: false,
        success: function success(resultsData, status) {
            var results = JSON.parse(resultsData);
            if (results.success === 'true') {
                console.log('upload success');
                //close modal
                $('#closeModal').click();
                //$('#uploadModal').modal('hide');
            } else if (results.success === 'false') {
                console.log('logout failure');
                alert(results.errorMsg);
            }
        }
    });
});

function getFileUrl(sourceId) {
    var url;
    if (navigator.userAgent.indexOf('MSIE') >= 1) {
        // IE
        url = document.getElementById(sourceId).value;
    } else if (navigator.userAgent.indexOf('Firefox') > 0) {
        // Firefox
        url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
    } else if (navigator.userAgent.indexOf('Chrome') > 0) {
        // Chrome
        url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
    }
    return url;
}

function preImg(sourceId, targetId) {
    var url = getFileUrl(sourceId);
    var imgPre = document.getElementById(targetId);
    imgPre.src = url;
    imgPre.style.display = 'block';
}

//show modal uploading function
$('#loading').ajaxStart(function () {
    $(this).show();
}) //when uploading images, show the icon
.ajaxComplete(function () {
    $(this).hide();
}); //hide it when uploaded.

// dismiss login error message
$('#alertDiv').click(function () {
    $(this).addClass('hidden');
});

/** Helpers */
$.urlParam = function (name) {
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results == null) {
        return null;
    } else {
        return results[1] || 0;
    }
};

/** Plugin methods */
jQuery.extend({
    handleError: function handleError(s, xhr, status, e) {
        // If a local callback was specified, fire it
        if (s.error) {
            s.error.call(s.context || s, xhr, status, e);
        }

        // Fire the global callback
        if (s.global) {
            (s.context ? jQuery(s.context) : jQuery.event).trigger('ajaxError', [xhr, s, e]);
        }
    },
    createUploadIframe: function createUploadIframe(id, uri) {

        var frameId = 'jUploadFrame' + id;

        if (window.ActiveXObject) {
            if (jQuery.browser.version == '9.0') {
                io = document.createElement('iframe');
                io.id = frameId;
                io.name = frameId;
            } else if (jQuery.browser.version == '6.0' || jQuery.browser.version == '7.0' || jQuery.browser.version == '8.0') {

                var io = document.createElement('<iframe id="' + frameId + '" name="' + frameId + '" />');
                if (typeof uri == 'boolean') {
                    io.src = 'javascript:false';
                } else if (typeof uri == 'string') {
                    io.src = uri;
                }
            }
        } else {
            var io = document.createElement('iframe');
            io.id = frameId;
            io.name = frameId;
        }
        io.style.position = 'absolute';
        io.style.top = '-1000px';
        io.style.left = '-1000px';

        document.body.appendChild(io);

        return io;
    },
    ajaxUpload: function ajaxUpload(s, xml) {
        //if((fromFiles.nodeType&&!((fileList=fromFiles.files)&&fileList[0].name)))

        var uid = new Date().getTime(),
            idIO = 'jUploadFrame' + uid,
            _this = this;
        var jIO = $('<iframe name="' + idIO + '" id="' + idIO + '" style="display:none">').appendTo('body');
        var jForm = $('<form action="' + s.url + '" target="' + idIO + '" method="post" enctype="multipart/form-data"></form>').appendTo('body');
        var oldElement = $('#' + s.fileElementId);
        var newElement = $(oldElement).clone();
        $(oldElement).attr('id', 'jUploadFile' + uid);
        $(oldElement).before(newElement);
        $(oldElement).appendTo(jForm);

        this.remove = function () {
            if (_this !== null) {
                jNewFile.before(jOldFile).remove();
                jIO.remove();jForm.remove();
                _this = null;
            }
        };
        this.onLoad = function () {

            var data = $(jIO[0].contentWindow.document.body).text();

            try {

                if (data != undefined) {
                    data = eval('(' + data + ')');
                    try {

                        if (s.success) s.success(data, status);

                        // Fire the global callback
                        if (s.global) jQuery.event.trigger('ajaxSuccess', [xml, s]);
                        if (s.complete) s.complete(data, status);
                        xml = null;
                    } catch (e) {

                        status = 'error';
                        jQuery.handleError(s, xml, status, e);
                    }

                    // The request was completed
                    if (s.global) jQuery.event.trigger('ajaxComplete', [xml, s]);
                    // Handle the global AJAX counter
                    if (s.global && ! --jQuery.active) jQuery.event.trigger('ajaxStop');

                    // Process result
                }
            } catch (ex) {
                alert(ex.message);
            };
        };
        this.start = function () {
            jForm.submit();jIO.load(_this.onLoad);
        };
        return this;
    },
    createUploadForm: function createUploadForm(id, url, fileElementId, data) {
        //create form
        var formId = 'jUploadForm' + id;
        var fileId = 'jUploadFile' + id;
        var form = jQuery('<form  action="' + url + '" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
        if (data) {
            for (var i in data) {
                jQuery('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
            }
        }

        var oldElement = jQuery('#' + fileElementId);
        var newElement = jQuery(oldElement).clone();
        jQuery(oldElement).attr('id', fileId);
        jQuery(oldElement).before(newElement);
        jQuery(oldElement).appendTo(form);

        //set attributes
        jQuery(form).css('position', 'absolute');
        jQuery(form).css('top', '-1200px');
        jQuery(form).css('left', '-1200px');
        jQuery(form).appendTo('body');
        return form;
    },
    ajaxFileUpload: function ajaxFileUpload(s) {
        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
        // Create the request object
        var xml = {};
        s = jQuery.extend({}, jQuery.ajaxSettings, s);
        if (window.ActiveXObject) {
            var upload = new jQuery.ajaxUpload(s, xml);
            upload.start();
        } else {
            var id = new Date().getTime();
            var form = jQuery.createUploadForm(id, s.url, s.fileElementId, typeof s.data == 'undefined' ? false : s.data);
            var io = jQuery.createUploadIframe(id, s.secureuri);
            var frameId = 'jUploadFrame' + id;
            var formId = 'jUploadForm' + id;
            // Watch for a new set of requests
            if (s.global && !jQuery.active++) {
                jQuery.event.trigger('ajaxStart');
            }
            var requestDone = false;

            if (s.global) jQuery.event.trigger('ajaxSend', [xml, s]);
            // Wait for a response to come back
            var uploadCallback = function uploadCallback(isTimeout) {
                var io = document.getElementById(frameId);

                try {
                    if (io.contentWindow) {
                        xml.responseText = io.contentWindow.document.body ? io.contentWindow.document.body.innerHTML : null;
                        xml.responseXML = io.contentWindow.document.XMLDocument ? io.contentWindow.document.XMLDocument : io.contentWindow.document;
                    } else if (io.contentDocument) {
                        xml.responseText = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
                        xml.responseXML = io.contentDocument.document.XMLDocument ? io.contentDocument.document.XMLDocument : io.contentDocument.document;
                    }
                } catch (e) {
                    jQuery.handleError(s, xml, null, e);
                }
                if (xml || isTimeout == 'timeout') {
                    requestDone = true;
                    var status;
                    try {
                        status = isTimeout != 'timeout' ? 'success' : 'error';
                        // Make sure that the request was successful or notmodified
                        if (status != 'error') {
                            // process the data (runs the xml through httpData regardless of callback)
                            var data = jQuery.uploadHttpData(xml, s.dataType);
                            // If a local callback was specified, fire it and pass it the data

                            if (s.success) s.success(data, status);

                            // Fire the global callback
                            if (s.global) jQuery.event.trigger('ajaxSuccess', [xml, s]);
                            if (s.complete) s.complete(data, status);
                        } else jQuery.handleError(s, xml, status);
                    } catch (e) {
                        status = 'error';
                        jQuery.handleError(s, xml, status, e);
                    }

                    // The request was completed
                    if (s.global) jQuery.event.trigger('ajaxComplete', [xml, s]);
                    // Handle the global AJAX counter
                    if (s.global && ! --jQuery.active) jQuery.event.trigger('ajaxStop');

                    // Process result
                    jQuery(io).unbind();

                    setTimeout(function () {
                        try {
                            jQuery(io).remove();
                            jQuery(form).remove();
                        } catch (e) {
                            jQuery.handleError(s, xml, null, e);
                        }
                    }, 100);

                    xml = null;
                }
            };
            // Timeout checker
            if (s.timeout > 0) {
                setTimeout(function () {
                    // Check to see if the request is still happening
                    if (!requestDone) uploadCallback('timeout');
                }, s.timeout);
            }

            try {

                var form = jQuery('#' + formId);
                jQuery(form).attr('action', s.url);
                jQuery(form).attr('method', 'POST');
                jQuery(form).attr('target', frameId);

                if (form.encoding) {
                    jQuery(form).attr('encoding', 'multipart/form-data');
                } else {
                    jQuery(form).attr('enctype', 'multipart/form-data');
                }

                jQuery(form).submit();
            } catch (e) {
                jQuery.handleError(s, xml, null, e);
            }

            jQuery('#' + frameId).on(uploadCallback);
            return { abort: function abort() {} };
        }
    },

    uploadHttpData: function uploadHttpData(r, type) {

        var data = !type;
        data = type == 'xml' || data ? r.responseXML : r.responseText;
        // If the type is "script", eval it in global context
        if (type == 'script') jQuery.globalEval(data);
        // Get the JavaScript object, if JSON is used.
        if (type == 'json') {

            eval('data = ' + $(data).html());
        }
        // evaluate scripts within html
        if (type == 'html') jQuery('<div>').html(data).evalScripts();

        return data;
    }
});
$('#signupForm').validate();
$('#loginForm').validate();

function layout(masonryContainer) {
    masonryContainer.imagesLoaded(function () {
        masonryContainer.masonry({
            columnWidth: '.item',
            itemSelector: '.item'
        });
    });
}
//# sourceMappingURL=main.js.map
