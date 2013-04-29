<?php
 
$earth_radius = 3960.00; # in miles
$lat_1 = "38.20151";
$lon_1 = "21.74324";
$lat_2 = "38.20625";
$lon_2 = "21.73738";
$delta_lat = $lat_2 - $lat_1 ;
$delta_lon = $lon_2 - $lon_1 ;
 
function distance_haversine($lat1, $lon1, $lat2, $lon2) {
  global $earth_radius;
  global $delta_lat;
  global $delta_lon;
  $alpha    = $delta_lat/2;
  $beta     = $delta_lon/2;
  $a        = sin(deg2rad($alpha)) * sin(deg2rad($alpha)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * sin(deg2rad($beta)) * sin(deg2rad($beta)) ;
  $c        = asin(min(1, sqrt($a)));
  $distance = 2*$earth_radius * $c;
  $distance = round($distance, 4);
 
  return $distance;
}
 
$hav_distance = distance_haversine($lat_1, $lon_1, $lat_2, $lon_2);
echo $hav_distance;
?>