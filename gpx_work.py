import math
import gpxpy

# Specify the path to your GPX file
gpx_file_path = "malham.gpx"

# Open the GPX file
with open(gpx_file_path, "r") as gpx_file:
    # Parse the GPX data
    gpx_data = gpxpy.parse(gpx_file)


# Get the number of points in the GPX data
num_points = len(gpx_data.tracks[0].segments[0].points)

# Create a list to store the change in lat/long values in metres
distances = []
elevations = []
for i in range(num_points - 1):
    # Get the current and next point
    current_point = gpx_data.tracks[0].segments[0].points[i]
    next_point = gpx_data.tracks[0].segments[0].points[i + 1]

    # Calculate the change in lat/long values in metres
    lat_change = math.radians(next_point.latitude - current_point.latitude) * 6371000
    long_change = math.radians(next_point.longitude - current_point.longitude) * 6371000

    distance = math.sqrt(lat_change**2 + long_change**2)

    if distance != 0:
        distances.append(distance)
        elevations.append(next_point.elevation - current_point.elevation)

print(distances)
print(elevations)

# Calculate the total distance travelled
total_distance = sum(distances)

work = 0 # (J)
mass = 65 # change (kg)
g = 9.81 # (m/s^2)

for i in range(len(distances)):
    actual_distance = math.sqrt(distances[i]**2 + elevations[i]**2)
    work += mass * g * math.sin(math.atan(elevations[i]/distances[i])) * actual_distance
    print(work)

print(work)


