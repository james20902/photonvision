/*
 * Copyright (C) 2020 Photon Vision.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.photonvision.vision.pipeline.result;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import java.util.Objects;
import org.photonvision.common.dataflow.structures.Packet;
import org.photonvision.vision.target.TrackedTarget;

public class SimpleTrackedTarget {
    public static final int PACK_SIZE_BYTES = Double.BYTES * 7;

    private double yaw;
    private double pitch;
    private double area;
    private double skew;
    private Pose2d robotRelativePose = new Pose2d();

    public SimpleTrackedTarget() {}

    public SimpleTrackedTarget(double yaw, double pitch, double area, double skew, Pose2d pose) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.area = area;
        this.skew = skew;
        robotRelativePose = pose;
    }

    public SimpleTrackedTarget(TrackedTarget t) {
        this(t.getYaw(), t.getPitch(), t.getArea(), t.getSkew(), t.getRobotRelativePose());
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getArea() {
        return area;
    }

    public Pose2d getRobotRelativePose() {
        return robotRelativePose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTrackedTarget that = (SimpleTrackedTarget) o;
        return Double.compare(that.yaw, yaw) == 0
                && Double.compare(that.pitch, pitch) == 0
                && Double.compare(that.area, area) == 0
                && Objects.equals(robotRelativePose, that.robotRelativePose);
    }

    @Override
    public int hashCode() {
        return Objects.hash(yaw, pitch, area, robotRelativePose);
    }

    /**
    * Populates the fields of this class with information from the incoming packet.
    *
    * @param packet The incoming packet.
    * @return The incoming packet.
    */
    public Packet createFromPacket(Packet packet) {
        yaw = packet.decodeDouble();
        pitch = packet.decodeDouble();
        area = packet.decodeDouble();
        skew = packet.decodeDouble();

        double x = packet.decodeDouble();
        double y = packet.decodeDouble();
        double r = packet.decodeDouble();

        robotRelativePose = new Pose2d(x, y, Rotation2d.fromDegrees(r));

        return packet;
    }

    /**
    * Populates the outgoing packet with information from the current target.
    *
    * @param packet The outgoing packet.
    * @return The outgoing packet.
    */
    public Packet populatePacket(Packet packet) {
        packet.encode(yaw);
        packet.encode(pitch);
        packet.encode(area);
        packet.encode(skew);
        packet.encode(robotRelativePose.getTranslation().getX());
        packet.encode(robotRelativePose.getTranslation().getY());
        packet.encode(robotRelativePose.getRotation().getDegrees());

        return packet;
    }
}
