package org.chromium.chrome.browser;

import org.chromium.base.Log;
import org.chromium.base.ContextUtils;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.preferences.Pref;

import java.net.URL;
import java.net.MalformedURLException;

public class PersonalizeResults {
    public static void Execute(Tab tab) {
       final boolean shouldRewrapText = ContextUtils.getAppSharedPreferences().getBoolean("text_rewrap", false);
       final boolean shouldRemoveAmp = ContextUtils.getAppSharedPreferences().getBoolean("avoid_amp_websites", true);
       if (shouldRemoveAmp && tab != null && IsSearchUrl(tab.getUrl().getSpec())) {
          tab.getWebContents().evaluateJavaScript(AMP_SCRIPT, null);
       }
       if (tab != null && shouldRewrapText) {
          tab.getWebContents().evaluateJavaScript("(function() { var pendingUpdate=false;function viewportHandler(event){if(pendingUpdate)return;pendingUpdate=true;requestAnimationFrame(()=>{pendingUpdate=false;document.getElementsByTagName('html')[0].style.maxWidth=window.visualViewport.width+'px';var miniLeft=visualViewport.offsetLeft;var miniTop = -(visualViewport.offsetTop + visualViewport.offsetTop * ((window.pageYOffset / window.innerHeight) / 2));document.getElementsByTagName('html')[0].style.transition='0s ease-in-out';if (miniLeft == 0 && miniTop == 0) { document.getElementsByTagName('html')[0].style.transform=''; } else { document.getElementsByTagName('html')[0].style.transform='translate('+miniLeft+'px, '+miniTop+'px) scale(1.0)'; } })}window.visualViewport.addEventListener('resize',viewportHandler);window.visualViewport.addEventListener('scroll', viewportHandler); })();", null);
       }
       if (tab != null && tab.getUrl().getSpec().startsWith("https://chrome.google.com/webstore")) {
          tab.getWebContents().evaluateJavaScript("(function() { if (!document.location.href.includes('https://chrome.google.com/webstore')) { return; } " + MAKE_USER_AGENT_WRITABLE + " window.navigator.userAgent='Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.25 Safari/537.36'; window.addEventListener('load', function() { if (document.location.pathname == '/webstore/unsupported') { document.location = '/webstore/'; } var node = document.createElement('style');    document.body.appendChild(node);    window.addStyleString = function(str) {        node.innerHTML = str;    }; addStyleString('div { visibility: visible !important; } '); var t=document.querySelector('meta[name=\"viewport\"]');t&&(t.content=\"initial-scale=0.1\",t.content=\"width=1200\") }); })();", null);
       }
        if (tab != null && ContextUtils.getAppSharedPreferences().getBoolean("cws_mobile_friendly", true) &&tab.getUrl().getSpec().startsWith("https://chrome.google.com/webstore")) {
          tab.getWebContents().evaluateJavaScript(CWS_MOBILE_SCRIPT, null);
       }
       if (tab != null && tab.getUrl().getSpec().startsWith("https://microsoftedge.microsoft.com/addons")) {
          tab.getWebContents().evaluateJavaScript(EDGE_SCRIPT, null);
       }
       if (tab != null && (tab.getUrl().getSpec().startsWith("https://m.facebook.com/messenger/install")
                       || tab.getUrl().getSpec().startsWith("https://m.facebook.com/messages"))) {
          tab.getWebContents().evaluateJavaScript(MESSENGER_SCRIPT, null);
       }
       if (tab != null && tab.getUrl().getSpec().contains("messenger.com/")) {
          tab.getWebContents().evaluateJavaScript(MESSENGER_VIEWPORT_SCRIPT, null);
       }
       if (tab != null && tab.getUrl().getSpec().startsWith("https://m.facebook.com/")) {
          tab.getWebContents().evaluateJavaScript("(function(){ if (!document.location.href.includes('https://m.facebook.com/')) { return; } document.querySelector('body.touch').style = \"cursor:default\";})();", null);
       }
       if (tab != null && tab.getUrl().getSpec().startsWith("https://translate.google.com/translate_c")) {
          tab.getWebContents().evaluateJavaScript("(function(){ if (!document.location.href.includes('https://translate.google.com/translate_c')) { return; } var b=document.getElementById(\"gt-nvframe\");if(b){b.style.position='unset';document.body.style.top='0px'}else{var child=document.createElement('iframe');child.id='gt-nvframe';child.src=document.location.href.replace('/translate_c','/translate_nv');child.style.width='100%';child.style.height='93px';document.body.insertBefore(child,document.body.firstChild);var t=document.querySelector('meta[name=\"viewport\"]');if(!t){var metaTag=document.createElement('meta');metaTag.name='viewport';metaTag.content='width=device-width, initial-scale=1.0';document.body.appendChild(metaTag)}}})();", null);
       }
       if (tab != null && (tab.getUrl().getSpec().startsWith("chrome://")
                       || tab.getUrl().getSpec().startsWith("chrome-extension://")
                       || tab.getUrl().getSpec().startsWith("kiwi://"))) {
          tab.getWebContents().evaluateJavaScript("(function() { if (!document.location.href.includes('chrome://') && !document.location.href.includes('chrome-extension://') && !document.location.href.includes('kiwi://')) { return; } " + ADAPT_TO_MOBILE_VIEWPORT + "})();", null);
       }
       if (tab != null && ContextUtils.getAppSharedPreferences().getBoolean("accept_cookie_consent", true) && (tab.getUrl().getSpec().startsWith("http://") || tab.getUrl().getSpec().startsWith("https://"))) {
          tab.getWebContents().evaluateJavaScript("(function(){function clickItem(elem) { elem.click(); } function acceptViaAPIs(){typeof window.__cmpui=='function'&&window.__cmpui('setAndSaveAllConsent',!0);typeof window.Didomi=='object'&&window.Didomi.setUserAgreeToAll()}window.globalObserver=null;function setupObserver(){if(!window.globalObserver){var newelem=document.createElement('style');newelem.innerHTML='.qc-cmp-showing { visibility: hidden !important; } body.didomi-popup-open { overflow: auto !important; } #didomi-host { visibility: hidden !important; }';document.body.appendChild(newelem);var MutationObserver=window.MutationObserver||window.WebKitMutationObserver;window.globalObserver=new MutationObserver(check);window.globalObserver.observe(window.document.documentElement,{childList:true,subtree:true});window.setTimeout(function(){window.globalObserver.disconnect();window.globalObserver=null},15000)}check()}function check(){window.setTimeout(function(){var listeners=[];listeners.push({selector:'#qcCmpUi',fn:acceptViaAPIs});listeners.push({selector:'#didomi-popup',fn:acceptViaAPIs});listeners.push({selector: '.accept-cookies-button,#purch-gdpr-banner__accept-button,#bbccookies-continue-button,.user-action--accept,.consent-accept,.bcpConsentOKButton,.button.accept,#footer_tc_privacy_button,button[aria-label=\"Button to collapse the message\"],.gdpr-form>.btn[value=\"Continue\"],button[on^=\"tap:\"][on$=\".accept\"],button[on^=\"tap:\"][on$=\".dismiss\"],.js-cookies-button,.app-offer__close_js,.lg-cc__button_type_action',fn: clickItem});for(var i=0,len=listeners.length,listener,elements;i<len;i++){listener=listeners[i];elements=window.document.querySelectorAll(listener.selector);for(var j=0,jLen=elements.length,element;j<jLen;j++){element=elements[j];if(!element.ready){element.ready=true;listener.fn.call(element, element)}}}},5)}window.addEventListener('DOMContentLoaded',setupObserver);check()})();", null);
       }
    }

    private static boolean IsSearchUrl(String sUrl) {
        if (sUrl == null || sUrl.isEmpty()) {
          return false;
        }

        try {
          URL url = new URL(sUrl);
            String sHost = url.getHost();
            if (sHost.contains(".google."))
                return true;
        } catch(MalformedURLException e) {
          Log.w("Kiwi", "MalformedURLException "+ e.getMessage());
        }

        return false;
    }

    private static final String MAKE_USER_AGENT_WRITABLE = ""
+"(function() {"
+"    function createProperty(value) {"
+"        var _value = value;"
+""
+"        function _get() {"
+"            return _value"
+"        }"
+""
+"        function _set(v) {"
+"            _value = v"
+"        }"
+"        return {"
+"            'get': _get,"
+"            'set': _set"
+"        }"
+"    };"
+""
+"    function makePropertyWritable(objBase, objScopeName, propName, initValue) {"
+"        var newProp, initObj;"
+"        if (objBase && objScopeName in objBase && propName in objBase[objScopeName]) {"
+"            if (typeof initValue === 'undefined') {"
+"                initValue = objBase[objScopeName][propName]"
+"            }"
+"            newProp = createProperty(initValue);"
+"            try {"
+"                Object.defineProperty(objBase[objScopeName], propName, newProp)"
+"            } catch (e) {"
+"                initObj = {};"
+"                initObj[propName] = newProp;"
+"                try {"
+"                    objBase[objScopeName] = Object.create(objBase[objScopeName], initObj)"
+"                } catch (e) {}"
+"            }"
+"        }"
+"    };"
+"    makePropertyWritable(window, 'navigator', 'userAgent');"
+"})();";

    private static final String ADAPT_TO_MOBILE_VIEWPORT = ""
+"(function() {"
+"window.addEventListener('load', function() {"
+"    var t = document.querySelector('meta[name=\"viewport\"]');"
+"    t && (t.content = 'initial-scale=1', t.content = 'width=device-width');"
+"    if (!t) {"
+"        var metaTag = document.createElement('meta');"
+"        metaTag.name = 'viewport';"
+"        metaTag.content = 'width=device-width, initial-scale=1.0';"
+"        document.body.appendChild(metaTag);"
+"    }"
+"});"
+"})();";

    private static final String CWS_MOBILE_SCRIPT = ""
+"(function() {"
+"var x = document.getElementsByClassName('O-j-gb');var node = document.createElement('style');"
+"node.innerHTML ='.a-t-o-k {width:auto;}.a-K-o {padding-top:8px!important;}.h-a-x {text-align: center;}.a-t-o-ea {padding-left:10px}.a-t-o-A {width: calc(100vw - 40px)!important;border-radius: 8px;}.menubtn{float:left;padding:4px}.HWJfBb{height:90vh!important;overflow-y:scroll!important;box-shadow:0 3px 6px rgba(0,0,0,.16),0 3px 6px rgba(0,0,0,.23)!important}.h-e-f-Ra-c{position:fixed!important;top:68px!important;left:0px!important;z-index:10000!important}div .a-P-d-k{margin:5px!important;border-radius:5px;padding:5px;box-shadow:0 3px 6px rgba(0,0,0,.16),0 3px 6px rgba(0,0,0,.23)}.e-f-o{padding-top:50px!important}.e-f-n-Va{margin:0 auto!important}.e-f-b.g-b{margin:0 4px!important;padding:6px 7px!important}.e-f-w-Va,.e-f-yb-w{width:calc(100vw - 80px)!important}.back-to-top{width:100px;height:130px;padding:10px;text-align:center;background:whiteSmoke;font-weight:700;color:#444;text-decoration:none;position:fixed;top:75px;right:40px;display:none;background:url(arrow_up.png) no-repeat 0 20px}.back-to-top:hover{text-decoration:none}.yrk3fc{margin:10px 16px!important}span .h-n-j-Z-ea-aa{margin-top:10px!important}img.hA-Ce-ze-Yf,.C-b-i .i-da-A{width:100vw!important}.e-f-Ib-n,.e-f-Ib-p{display:none}.O-j-Ic-c{display:none}.F-x{width:100%!important}img.a-P-d-A{height:100px!important;width:160px!important}.menubutton{display:inline-block;cursor:pointer}.bar1,.bar2,.bar3{width:25px;height:4px;background-color:#5f6368;margin:3px 0;margin-right:6px;transition:0.4s}.change .bar1{-webkit-transform:rotate(-45deg) translate(-5px,5px);transform:rotate(-45deg) translate(-5px,5px)}.change .bar2{opacity:0}.change .bar3{-webkit-transform:rotate(45deg) translate(-5px,-5px);transform:rotate(45deg) translate(-5px,-5px)}.a-La{display:none}.Je-qe-zd-Ge{width:100vw!important}.F-k,.a-P-d{width:auto!important}.PNF6le{text-transform:none!important}div .a-d.webstore-test-wall-tile.a-d-zc.a-P-d-k{width:160px!important}';"
+"document.body.appendChild(node);var menuStyle = document.createElement('style');"
+"menuStyle.innerHTML = '.F-n-J{display:none!important;position:fixed!important;right: 0px!important;height: 100vh!important;width: 100vw!important;max-width: 450px;z-index: 1149!important;}';"
+"document.body.appendChild(menuStyle);var showMenu=0;var menuBtn = document.createElement('div');"
+"menuBtn.className = 'menubtn';menuBtn.innerHTML= '<div class=\"bar1\"></div><div class=\"bar2\"></div><div class=\"bar3\"></div>';"
+" menuBtn.onclick=function(){menuBtn.classList.toggle(\"change\"),menuStyle.innerHTML=\".F-n-J{display:\"+(showMenu?\"none\":\"inline-block\")+\"!important;position:fixed!important;left:0px!important;width: 100vw!important;max-width: 450px;background:none!important;z-index: 1149!important;}\",showMenu=!showMenu};"
+"x[0].insertBefore(menuBtn, x[0].firstChild);"
+"})();";

    private static final String MESSENGER_SCRIPT = ""
+"(function() {"
+"window.addEventListener('load', function() {"
+"if (!document.location.href.includes('https://m.facebook.com/messenger/install') && !document.location.href.includes('https://m.facebook.com/messages')) { return; } "
+"var gotomessenger = document.createElement('div');"
+"gotomessenger.innerHTML = \"<a href='https://www.messenger.com' target='_blank' style='margin: 2rem; display: inline-block;'><b>Go to www.messenger.com instead</a>\";"
+"gotomessenger.id = '_kb_gotomessenger';"
+"var e1 = document.querySelector('._8rws') || document.querySelector('._2bu8');"
+"if (!document.getElementById('_kb_gotomessenger'))"
+"e1.parentNode.insertBefore(gotomessenger, e1.nextSibling);"
+"});"
+"})();";

    private static final String MESSENGER_VIEWPORT_SCRIPT = ""
+"(function() {"
+"if (!document.location.href.includes('messenger.com/')) { return; } "
+"var sheet = document.createElement('style');"
+"sheet.innerHTML = '.jgljxmt5 { min-height: calc(93vh - var(--header-height)); } .g0mhvs5p.g0mhvs5p { width: 150px; }';"
+"document.body.appendChild(sheet);"
+"})();";

    private static final String AMP_SCRIPT = ""
+"(function() {"
+"function _cleanupAmp()"
+"{"
+"  document.querySelectorAll('a[data-amp-cur]').forEach(function(a) {"
+"    a.href = a.getAttribute('data-amp-cur');"
+"    console.log('Detected AMP item: (link: ' + a.href + ')');"
+"    if (!a.href || a.href.includes('/search')) { a.href = a.getAttribute('data-amp'); console.log('Corrected AMP item: (link: ' + a.href + ')'); }"
+"    if (a.href.indexOf('?') == -1) { a.href = a.href + '?'; }"
+"    a.removeAttribute('data-amp');"
+"    a.removeAttribute('data-amp-cur');"
+"    a.removeAttribute('ping');"
+"  });"
+""
+"  document.querySelectorAll('span[aria-label=\"AMP logo\"]').forEach(function(a) {"
+"     a.style.display='none';"
+"  });"
+"  if (document.getElementsByClassName('amp-cantxt').length >= 1 && document.location.href.match(/\\/amp[\\/|\\.]/)) { document.location.replace(document.getElementsByClassName('amp-cantxt')[0].innerText); }"
+"  if (document.location.href.match(/\\/amp[\\/|\\.]/) && document.querySelector('head > link[rel=\"canonical\"]') != null && document.querySelector('head > link[rel=\"canonical\"]').href != document.location.href) { document.location.replace(document.querySelector('head > link[rel=\"canonical\"]').href); };"
+"}"
+""
+"document.addEventListener('DOMNodeInserted', _cleanupAmp);"
+"_cleanupAmp();"
+"})();";

    private static final String EDGE_SCRIPT = ""
+"(function() {"
+"    if (!document.location.href.includes('https://microsoftedge.microsoft.com/addons')) {"
+"        return;"
+"    }"
+ MAKE_USER_AGENT_WRITABLE
+"    window.navigator.userAgent=window.navigator.userAgent + ' Edg/' + window.navigator.appVersion.match(/Chrome\\/(\\d+(:?\\.\\d+)+)/)[1];"
+"    var _kb_setIntervalCnt = 0;"
+"    var _kb_setInterval = window.setInterval(function() {"
+"        var xpath = function(xpathToExecute) {"
+"            var result = [];"
+"            var nodesSnapshot = document.evaluate(xpathToExecute, document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);"
+"            for (var i = 0; i < nodesSnapshot.snapshotLength; i++) {"
+"                result.push(nodesSnapshot.snapshotItem(i));"
+"            }"
+"            return result;"
+"        };"
+"        xpath(\"//button[contains(@id,'getOrRemoveButton')]\").forEach(function(individualButton) {"
+"            individualButton.setAttribute('style', 'opacity: 1; background: rgb(0, 120, 212) !important; height: 60px; cursor: pointer !important;');"
+"            individualButton.removeAttribute('disabled');"
+"            individualButton.innerHTML = \"<a href=https://edge.microsoft.com/extensionwebstorebase/v1/crx?response=redirect&acceptformat=crx3&x=id%3D\" + individualButton.id.split('-')[1] + \"%26installsource%3Dondemand%26uc target='_blank' style='color: white; text-decoration: none'><b>Get CRX</b><br>(Hold and tap<br>Download Link)</a>\";"
+"        });"
+"        if (_kb_setIntervalCnt++ >= 10) { window.clearInterval(_kb_setInterval); }"
+"    }, 1000);"
+"})();";
}
