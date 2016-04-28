<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<IndoorFeatures xmlns="http://www.opengis.net/indoorgml/1.0/core" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" gml:id="IFs" xsi:schemaLocation="http://www.opengis.net/indoorgml/1.0/core http://schemas.opengis.net/indoorgml/1.0/indoorgmlcore.xsd">
    <gml:name>IFs</gml:name>
    <gml:boundedBy xsi:nil="true"/>
    <primalSpaceFeatures>
        <PrimalSpaceFeatures gml:id="PS1">
            <gml:name>PS1</gml:name>
            <gml:boundedBy xsi:nil="true"/>
            <cellSpaceMember>
                <CellSpace gml:id="C1">
                    <gml:name>C1</gml:name>
                    <Geometry2D>
                        <gml:Polygon gml:id="POLY1">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:pos>-123.395 5.79999 0.0</gml:pos>
                                    <gml:pos>-123.395 11.82499 0.0</gml:pos>
                                    <gml:pos>-126.39502 11.82499 0.0</gml:pos>
                                    <gml:pos>-126.39502 5.79999 0.0</gml:pos>
                                    <gml:pos>-123.395 5.79999 0.0</gml:pos>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </Geometry2D>
                    <duality xlink:href="#R1"/>
                </CellSpace>
            </cellSpaceMember>
            <cellSpaceMember>
                <CellSpace gml:id="C2">
                    <gml:name>C2</gml:name>
                    <Geometry2D>
                        <gml:Polygon gml:id="POLY2">
                            <gml:exterior>
                                <gml:LinearRing>
                                    <gml:pos>-120.69501 -1.67251 0.0</gml:pos>
                                    <gml:pos>-120.69501 1.99999 0.0</gml:pos>
                                    <gml:pos>-123.195 1.99999 0.0</gml:pos>
                                    <gml:pos>-123.195 -1.67251 0.0</gml:pos>
                                    <gml:pos>-120.69501 -1.67251 0.0</gml:pos>
                                </gml:LinearRing>
                            </gml:exterior>
                        </gml:Polygon>
                    </Geometry2D>
                    <duality xlink:href="#R2"/>
                </CellSpace>
            </cellSpaceMember>
        </PrimalSpaceFeatures>
    </primalSpaceFeatures>
    <MultiLayeredGraph gml:id="MLG1">
        <gml:name xsi:nil="true"/>
        <spaceLayers gml:id="SL1">
            <gml:name>SL1</gml:name>
            <gml:boundedBy xsi:nil="true"/>
            <spaceLayerMember>
                <SpaceLayer gml:id="IS1">
                    <gml:name>IS1</gml:name>
                    <gml:boundedBy xsi:nil="true"/>
                    <nodes gml:id="N1">
                        <gml:name>N1</gml:name>
                        <stateMember>
                            <State gml:id="R1">
                                <gml:name>R1</gml:name>
                                <gml:boundedBy xsi:nil="true"/>
                                <duality xlink:href="#C1"/>
                                <geometry>
                                    <gml:Point gml:id="P16">
                                        <gml:name>P16</gml:name>
                                        <gml:pos>-124.89501 8.81249 0.0</gml:pos>
                                    </gml:Point>
                                </geometry>
                            </State>
                        </stateMember>
                        <stateMember>
                            <State gml:id="R2">
                                <gml:name>R2</gml:name>
                                <duality xlink:href="#C2"/>
                                <geometry>
                                    <gml:Point gml:id="P32">
                                        <gml:name>P32</gml:name>
                                        <gml:pos>-121.945 0.16374 0.0</gml:pos>
                                    </gml:Point>
                                </geometry>
                            </State>
                        </stateMember>
                    </nodes>
					<edges gml:id="E1">
						<gml:name>E1</gml:name>
						<transitionMember>
							<Transition gml:id="T1">
								<connects xlink:href="#R1"/>
								<connects xlink:href="#R2"/>
								<geometry>
                                    <gml:LineString gml:id="L1">
                                        <gml:name>L1</gml:name>
                                        <gml:posList>-124.89501 8.81249 0.0 -121.945 0.16374 0.0</gml:posList>
                                    </gml:LineString>
                                </geometry>
							</Transition>
						</transitionMember>
					</edges>
                </SpaceLayer>
            </spaceLayerMember>
        </spaceLayers>
    </MultiLayeredGraph>
</IndoorFeatures>
