<?php

//Configuration of icons to replace 
// name of icon -> [root path , icon type , output file name ] 
/*
array(
        "clock-rotate-left"=>array("regular","ic_history_googblue_24dp.png"),
        "folder-arrow-down"=>array("regular","ic_downloads.png"),
        "folder-bookmark"=>array("regular","ic_bookmarks.png"),
        "rectangle-vertical-history"=>array("regular","ic_recent_tabs.png"),
        "puzzle-piece"=>array("regular","ic_extensions.png"),

        "share-nodes"=>array("regular","ic_share.png"),
        "file-magnifying-glass"=>array("regular","ic_find_in_page_r.png"),
        "language"=>array("regular","ic_translate_r.png"),
        "grid-2-plus"=>array("regular","ic_add_to_home_screen_r.png"),
        "display"=>array("regular","ic_desktop_mode.png"),
        "shield-xmark"=>array("regular","ic_adblock_off.png"),
        "shield-check"=>array("regular","ic_adblock_on.png"),

        "sun-bright"=>array("regular","ic_night_mode_off.png"),
        "moon"=>array("regular","ic_night_mode_on.png"),
        "rectangle-terminal"=>array("regular","ic_devtools.png"),
        "gear"=>array("regular","settings_cog.png"),
        "trash-can"=>array("regular","ic_clear_data.png"),
        "right-from-bracket"=>array("regular","ic_exit_to_app_white_24dp.png")

    );

*/
$config = 

    array(
        "magnifying-glass"=>array("regular","ics_search_engine.png"),
        "edit-homepage"=>array("regular","ics_edit_homepage.png"),
        "lock-keyhole"=>array("regular","ics_passwords.png"),
        "languages"=>array("regular","ics_languages.png","w"),
        "credit-card"=>array("regular","ics_payments.png"),
        "map-location-dot"=>array("regular","ics_addresses.png"),
        "palette"=>array("regular","ics_theme.png"),
        "moon-stars"=>array("regular","ics_night_mode.png"),
        "rectangles-mixed"=>array("regular","ics_tab_switcher.png","w"),
        "language"=>array("regular","ic_translate_r.png","w"),
        "user-lock"=>array("regular","ics_privacy_security.png","w"),
        "website-settings"=>array("regular","ics_website_settings.png"),
        "trash-list"=>array("regular","ics_clear_data.png","w"),
        "universal-access"=>array("regular","ics_accessibility.png"),
        "bell"=>array("regular","ics_notifications.png"),
        "rocket-launch"=>array("regular","ics_lite_mode.png"),
        "brackets-curly"=>array("regular","ics_dev_options.png")
    );

foreach($config as $icon => $icon_param) {
    //'.$icon_param[0].'
    $source = '../fontawesome-pro/svgs/light/'.$icon.'.svg';
    if (!file_exists($source))
    {
        echo 'Source doesnt exists: ' . $source . "\n";
        die('php ' . $argv[0] . ' <image> <style> <target.png>' . "\n\n");
    }
    echo "------". $icon . "------";
    echo '\n Processing: ' . $source . "\n";


    $targets = glob("chrome/android/java/res/drawable*/" . $icon_param[1]);
    echo $targets;
    if(sizeof($targets)<2)
    $targets = glob("chrome/browser/ui/android/omnibox/java/res/drawable*/" . $icon_param[1]);
    if(sizeof($targets)<2)
    $targets = glob("components/browser_ui/styles/android/java/res/drawable*/" . $icon_param[1]);
    foreach ($targets as $target)
    {
        echo '\n Path of target : ' . $target . '\n';
        $size = getimagesize($target);
        $width = $size[0];
        $height = $size[1];
        $ssize = getimagesize($source);
        $sswidth = $ssize[0];
        $ssheight = $ssize[1];
        $swidth = intval($width - (16/100*$width));
        $sheight = intval($height - (16/100*$height));
        echo "   |   " . $icon_param[2]. "   |   ";
        if($icon_param[2] == "w")
            $line = 'rsvg-convert '. $source . ' -w ' . intval($swidth) . ' -o /tmp/tmp.png';
        else
            $line = 'rsvg-convert '. $source . ' -h ' . intval($sheight) . ' -o /tmp/tmp.png';
        //echo "\n" . $line . "\n";
        system($line);

        echo "\n - - - - - - - - \n";
        $line = 'convert /tmp/tmp.png -background none -gravity center -extent ' . intval($width) . 'x' . intval($height) . ' ' . $target;
        //echo $line . "\n";
        system($line);
    }
}