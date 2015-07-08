<html>
<head><title>Share my location</title></head>
<body>
  <div id="mapdiv"></div>
  <script src="http://www.openlayers.org/api/OpenLayers.js"></script>
<?php
    $string = file_get_contents("location.json");
    $json_a = json_decode($string, true)["userList"];
?>
  <script>

    map = new OpenLayers.Map("mapdiv");
    map.addLayer(new OpenLayers.Layer.OSM());

    epsg4326 =  new OpenLayers.Projection("EPSG:4326"); //WGS 1984 projection
    projectTo = map.getProjectionObject(); //The map projection (Spherical Mercator)

    var vectorLayer = new OpenLayers.Layer.Vector("Overlay");

    // Define an array of markers
    var markers = [ ];

<?php
$i = 0;
foreach ($json_a as $v) {
   echo "    var lon = \"" . $json_a[$i]["lon"] ."\";\n";
   echo "    var lat = \"" . $json_a[$i]["lat"] ."\";\n";
   echo "    var name = \"" . $json_a[$i]["user"]  . " " . date('H:i:s d.m.y', $json_a[$i]["time"]) . "\";\n";
   echo "    markers.push( [ lon, lat, name ]);\n";
   $i++;
}
?>

    var lonLat = new OpenLayers.LonLat(lon , lat).transform(epsg4326, projectTo);

    map.setCenter (lonLat, 12);

    //Loop through the markers array
    for (var i=0; i<markers.length; i++) {
       var lon = markers[i][0];
       var lat = markers[i][1];
       var description = markers[i][2];
       var feature = new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Point( lon, lat ).transform(epsg4326, projectTo),
                {description: description} ,
                {externalGraphic: 'img/marker.png', graphicHeight: 50, graphicWidth: 50, graphicXOffset:-25, graphicYOffset:-25  }
            );
        vectorLayer.addFeatures(feature);
    }

    map.addLayer(vectorLayer);

    //Add a selector control to the vectorLayer with popup functions
    var controls = {
      selector: new OpenLayers.Control.SelectFeature(vectorLayer, { onSelect: createPopup, onUnselect: destroyPopup })
    };

    function createPopup(feature) {
      feature.popup = new OpenLayers.Popup.FramedCloud("pop",
          feature.geometry.getBounds().getCenterLonLat(),
          null,
          '<div class="markerContent">'+feature.attributes.description+'</div>',
          null,
          true,
          function() { controls['selector'].unselectAll(); }
      );
      //feature.popup.closeOnMove = true;
      map.addPopup(feature.popup);
    }

    function destroyPopup(feature) {
      feature.popup.destroy();
      feature.popup = null;
    }

    map.addControl(controls['selector']);
    controls['selector'].activate();

  </script>
</body></html>
