package com.linkedin.feathr.core.config.producer.anchors;

import com.linkedin.feathr.core.config.producer.common.FeatureTypeConfig;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.MapUtils;


/**
 * Represents a feature config based on extractor by specifying the value part in the following fragment:
 * {@code <feature name>:
 * {
 *    type: type of the feature // optional
 *    parameters: parameters for the extractor to configure different extractor behavior per feature // optional
 *    defaultValue: default value of the feature // optional
 * }
 */
// TODO - 17615): Rename this to ExtractorBasedFeatureConfig
// This class is still used by Galene. We should renamed it in next major version bump.
public final class SimpleFeatureConfig extends FeatureConfig {
  /**
   * Legacy field. Feature name.
   */
  private final String _featureName;
  /**
   * Optional parameters for the extractor, to configure the extractor behavior for each feature. By default it's empty.
   */
  private final Map<String, String> _parameters;
  private final Optional<FeatureTypeConfig> _featureTypeConfig;
  private final Optional<String> _defaultValue;

  private String _configStr;
  /**
   * Constructor
   * @param featureName A user-defined MVEL expression specifying the feature
   */
  public SimpleFeatureConfig(String featureName) {
    this(featureName, null, null, Collections.emptyMap());
  }

  /**
   * Constructor
   */
  public SimpleFeatureConfig(String featureName, FeatureTypeConfig featureTypeConfig) {
    this(featureName, featureTypeConfig, null, Collections.emptyMap());
  }

  /**
   * Constructor
   */
  public SimpleFeatureConfig(String featureName, FeatureTypeConfig featureTypeConfig, String defaultValue,
      Map<String, String> parameters) {
    _featureName = featureName;
    _featureTypeConfig = Optional.ofNullable(featureTypeConfig);
    _defaultValue = Optional.ofNullable(defaultValue);
    _parameters = parameters;
    constructConfigStr();
  }

  private void constructConfigStr() {
    StringBuilder sb = new StringBuilder();
    sb.append(FeatureConfig.DEF).append(": ").append(_featureName).append("\n");
    _featureTypeConfig.ifPresent(t -> sb.append(FeatureConfig.TYPE).append(": ").append(t).append("\n"));
    _defaultValue.ifPresent(v -> sb.append(FeatureConfig.DEFAULT).append(": ").append(v).append("\n"));
    if (MapUtils.isNotEmpty(_parameters)) {
      sb.append(FeatureConfig.PARAMETERS).append(": {\n");
      _parameters.entrySet().stream().map(entry -> sb.append(String.format("%s = %s\n", entry.getKey(), entry.getValue())));
      sb.append("}\n");
    }
    _configStr = sb.toString();
  }

  /**
   * @Deprecated Use {@link #getFeatureName()} instead.
   */
  // TODO - 17615): Remove this API in next major release
  // This method is still used by Galene.
  @Deprecated
  public String getFeatureExpr() {
    return _featureName;
  }

  public String getFeatureName() {
    return _featureName;
  }

  @Override
  public Optional<FeatureTypeConfig> getFeatureTypeConfig() {
    return _featureTypeConfig;
  }

  @Override
  public Optional<String> getDefaultValue() {
    return _defaultValue;
  }

  @Override
  public Map<String, String> getParameters() {
    return _parameters;
  }

  // TODO - 10384) Galene is using this function in their processing code so we can not update now. We can fix this
  // in next major version bump.
  @Override
  public String toString() {
    return _featureName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleFeatureConfig that = (SimpleFeatureConfig) o;
    return Objects.equals(_featureName, that._featureName) && Objects.equals(_featureTypeConfig,
        that._featureTypeConfig) && Objects.equals(_defaultValue, that._defaultValue) && Objects.equals(_parameters, that._parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_featureName, _featureTypeConfig, _defaultValue, _parameters);
  }
}
